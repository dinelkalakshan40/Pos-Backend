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
import lk.ijse.dto.CustomerDTO;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/customer")
public class CustomerController extends HttpServlet {

     Connection connection;

    @Override
    public void init() {
        try {
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/cafe");
            this.connection = pool.getConnection();
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER_BO);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DataSource pool = (DataSource) getServletContext().getAttribute("dbPool");
        try {
            // Retrieve all customers
            ArrayList<CustomerDTO> customerList = customerBO.getAllCustomer(connection);

            // Create a Jsonb instance
            JsonbConfig config = new JsonbConfig().withFormatting(true);
            Jsonb jsonb = JsonbBuilder.create(config);

            // Convert the list of CustomerDTOs to JSON
            String customerJSON = jsonb.toJson(customerList);

            // Write the JSON to the response
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(customerJSON);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //check content type application/json
        System.out.println("hello");
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Content type must be application/json");
        }
        System.out.println("start try catch");
        try (var writer = resp.getWriter()){
            Jsonb jsonb = JsonbBuilder.create();
            CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            System.out.println("customerDTO "+customerDTO);
            boolean isSaved = customerBO.saveCustomer(customerDTO, connection);
            if (isSaved){
                System.out.println("Customer saved");
                writer.println("Customer saved");
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }else{

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

}
