package ru.rental.servic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.servic.service.CarService;

import java.io.IOException;

@WebServlet("/car")
@Component
public class CarServlet extends HttpServlet {

    private final CarService carService;

    @Autowired
    public CarServlet(CarService carService) {
        this.carService = carService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("allcar", carService.getAll());
        req.getRequestDispatcher("/WEB-INF/jsp/car.jsp").forward(req, resp);
    }
}
