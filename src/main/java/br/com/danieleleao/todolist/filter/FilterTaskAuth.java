package br.com.danieleleao.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.danieleleao.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, @SuppressWarnings("null") HttpServletResponse response, @SuppressWarnings("null") FilterChain filterChain)
            throws ServletException, IOException {


                var servletPath = request.getServletPath();
                if (servletPath.startsWith("/tasks/")) {
                    
                    //Pegar a autenticação (user e senha)
                    var authorization = request.getHeader("Authorization");

                    var authEncoder = authorization.substring("Basic".length()).trim();

                    byte[] authDecoder = Base64.getDecoder().decode(authEncoder);

                    var authString = new String(authDecoder);
                    System.out.println("Authorization");
                    System.out.println(authString);
                
                    String[] credentials = authString.split(":");
                    String username = credentials[0];
                    String password = credentials[1];   

                    //Validar user
                    var user = this.userRepository.findByUsername(username);
                    if (user == null) {
                        response.sendError(401);
                    } else {
                        //Validar senha
                        final var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                        if (passwordVerify.verified) {
                            request.setAttribute("idUser", user.getId());
                            filterChain.doFilter(request, response);
                        } else {
                            response.sendError(401);
                        }

                    }

                } else {
                    filterChain.doFilter(request, response);
                }

    }
    
}
