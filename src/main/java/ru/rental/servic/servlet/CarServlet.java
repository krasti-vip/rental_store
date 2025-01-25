package ru.rental.servic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.rental.servic.dto.CarDto;
import ru.rental.servic.service.CarService;

import java.io.IOException;

@WebServlet("/car")
public class CarServlet extends HttpServlet {

    private final CarService carService = new CarService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final var allCarDto = carService.getAll();

        resp.setContentType("text/html; charset=UTF-8");
        final var writer = resp.getWriter();

        for (CarDto carDto : allCarDto) {
            writer.write(carDto.getTitle() + " ");
        }

        writer.close();
    }
}
