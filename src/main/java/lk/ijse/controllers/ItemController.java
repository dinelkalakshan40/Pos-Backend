package lk.ijse.controllers;

import jakarta.json.JsonException;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.ItemBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.ItemDAO;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.dto.ItemDTO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/item")
public class ItemController extends HttpServlet {
    Connection connection;
    ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM_BO);


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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("loadAll".equals(req.getParameter("action"))) {
            loadAllItems(req, resp);
        } else if (req.getParameter("itemID") != null) {
            searchItemsByID(req, resp);
        } else {
            generateNewItemID(resp, req);
        }
    }

    private void generateNewItemID(HttpServletResponse resp, HttpServletRequest req) {
        try (var Writer = resp.getWriter()) {

            String newItemId = itemBO.generateNewItemID(connection);

            var jsonb = JsonbBuilder.create();
            resp.setContentType("application/json");

            jsonb.toJson(newItemId, Writer);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllItems(HttpServletRequest req, HttpServletResponse resp) {
        try (var writer = resp.getWriter()) {
            List<ItemDTO> itemDTOList = itemBO.getAllItem(connection);

            if (itemDTOList != null) {
                resp.setContentType("application/json");
                Jsonb jsonb = JsonbBuilder.create();
                jsonb.toJson(itemDTOList, writer);
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




    private void searchItemsByID(HttpServletRequest req, HttpServletResponse resp) {
        var itemID = req.getParameter("itemID");
        try (var writer = resp.getWriter()){
            ItemDTO item = itemBO.searchItem(itemID,connection);
            var jsonb = JsonbBuilder.create();
            resp.setContentType("application/json");
            jsonb.toJson(item,writer);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Content type must be application/json");
        }
        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            boolean isSaved = itemBO.saveItem(itemDTO, connection);

            if (isSaved) {
                writer.println("Item saved");
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {

                writer.println("Item not saved");
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
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);
            boolean isUpdated = itemBO.updateItem(id, itemDTO, connection);

            if (isUpdated) {
                writer.println("Item updated");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                writer.println("Item not updated");
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
            boolean isDeleted = itemBO.deleteItem(id, connection);
            if (isDeleted) {
                resp.getWriter().println("Item deleted");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.getWriter().println("Item not deleted");
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
