import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

@WebServlet(name = "Test", urlPatterns = {"/test"})
@MultipartConfig
public class TestServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        InputStream structureStream = req.getPart("structureFile").getInputStream();
        String structureContents = new Scanner(structureStream).useDelimiter("\\Z").next();
        out.println(structureContents);
        InputStream dictionaryStream = req.getPart("dictionaryFile").getInputStream();
        String dictionaryContents = new Scanner(dictionaryStream).useDelimiter("\\Z").next();
        out.println(dictionaryContents);
    }

}
