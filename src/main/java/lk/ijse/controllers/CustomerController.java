package lk.ijse.controllers;

import jakarta.json.JsonException;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.CustomerBO;
import lk.ijse.bo.custom.impl.CustomerBOImpl;
import lk.ijse.dto.CustomerDTO;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/customer")
public class CustomerController extends HttpServlet {

    Connection connection;
    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER_BO);


    @Override
    public void init() {
        try {
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/cafe");
            this.connection = pool.getConnection();


            //  customerBO = new CustomerBOImpl();
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("loadAll".equals(action)) {
            loadAllCustomers(req, resp);
        } else {
            generateNewCustomerID(resp, req);
        }
    }

    private void generateNewCustomerID(HttpServletResponse resp, HttpServletRequest req) {
        try (var Writer = resp.getWriter()) {

            String newCustomerId = customerBO.generateNewCustomerId(connection);

            var jsonb = JsonbBuilder.create();
            resp.setContentType("application/json");

            jsonb.toJson(newCustomerId, Writer);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllCustomers(HttpServletRequest req, HttpServletResponse resp) {
        try (var writer = resp.getWriter()) {
            List<CustomerDTO> customerDTOList = customerBO.getAllCustomer(connection);
            System.out.println("customerDTOList" + customerDTOList);
            if (customerDTOList != null) {
                resp.setContentType("application/json");
                Jsonb jsonb = JsonbBuilder.create();
                jsonb.toJson(customerDTOList, writer);
            } else {
                writer.write("No customers found");
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //check content type application/json

        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Content type must be application/json");
        }
        System.out.println("start try catch");
        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);

            boolean isSaved = customerBO.saveCustomer(customerDTO, connection);
            if (isSaved) {
                System.out.println("Customer saved");
                writer.println("Customer saved");
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {

                writer.println("Customer not saved");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (JsonException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Content type must be application/json");
        }
        try (var writer = resp.getWriter()) {
            var id = req.getParameter("id");
            Jsonb jsonb = JsonbBuilder.create();
            System.out.println("create jsonb");
            CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            boolean isUpdated = customerBO.updateCustomer(id, customerDTO, connection);

            if (isUpdated) {
                writer.println("Customer updated");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                writer.println("Customer not updated");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var id = req.getParameter("id");
        try {
            boolean isDeleted = customerBO.deleteCustomer(id, connection);
            if (isDeleted) {
                resp.getWriter().println("Customer deleted");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.getWriter().println("Customer not deleted");
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
