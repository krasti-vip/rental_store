package ru.rental.servic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.servic.service.BikeService;

import java.io.IOException;

@WebServlet("/bike")
@Component
public class BikeServlet extends HttpServlet {

    private final BikeService bikeService;

    @Autowired
    public BikeServlet(BikeService bikeService) {
        this.bikeService = bikeService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("allbike", bikeService.getAll());
        req.getRequestDispatcher("/WEB-INF/jsp/bike.jsp").forward(req, resp);
    }
}
