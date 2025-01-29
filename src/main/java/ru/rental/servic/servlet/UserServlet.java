package ru.rental.servic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.servic.service.UserService;

import java.io.IOException;

@WebServlet("/user")
@Component
public class UserServlet extends HttpServlet {

    private final UserService userService;

    @Autowired
    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("alluser", userService.getAll());
        try {
            req.getRequestDispatcher("/WEB-INF/jsp/user.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new IllegalStateException("Ошибка передачи данных", e);
        }
    }
}
