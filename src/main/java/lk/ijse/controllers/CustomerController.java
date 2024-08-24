package lk.ijse.controllers;

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
    private DataSource pool;
    private Connection connection;

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
            ArrayList<CustomerDTO> customerList = customerBO.getAllCustomer(pool);

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //check content type application/json
        if (!req.getContentType().equals("application/json")) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Content type must be application/json");
            return;
        }
        try {
            // Obtain a connection from the DataSource
            connection = pool.getConnection();

            // Read the JSON from the request body
            BufferedReader reader = req.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String json = jsonBuilder.toString();

            // Create a Jsonb instance and deserialize the JSON to a CustomerDTO object
            JsonbConfig config = new JsonbConfig().withFormatting(true);
            Jsonb jsonb = JsonbBuilder.create(config);
            CustomerDTO customerDTO = jsonb.fromJson(json, CustomerDTO.class);

            // Save the customer using the business logic
            boolean isSaved = customerBO.saveCustomer(customerDTO, pool);

            // Send response back to the client
            if (isSaved) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("{\"message\":\"Customer saved successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"message\":\"Failed to save customer\"}");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            // Always close the connection to prevent resource leaks
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
