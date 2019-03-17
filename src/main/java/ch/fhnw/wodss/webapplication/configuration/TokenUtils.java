package ch.fhnw.wodss.webapplication.configuration;

import ch.fhnw.wodss.webapplication.components.employee.Employee;
import ch.fhnw.wodss.webapplication.components.employee.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

public class TokenUtils {

    private static final String SECRET_KEY = "foobar";

    private static final long VALIDATION_DURATION = 30 * 60 * 1000;

    public static String createTokenForEmployee(Employee employee) {
        try {
            Date nowDate = new Date();
            Date expirationDate = new Date(nowDate.getTime() + VALIDATION_DURATION);
            return Jwts.builder()
                .setIssuer("FHNW wodss")
                .setSubject("Login token")
                .claim("employee", employee)
                .setIssuedAt(nowDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()))
                .compact();
        } catch (Exception exception) {
            throw new RuntimeException("Internal token error");
        }
    }

    public static Optional<Employee> getEmployeeFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(SECRET_KEY.getBytes())).parseClaimsJws(token).getBody();
            HashMap<String, String> map = (HashMap<String, String>) claims.get("employee");
            Employee employee = new Employee(map.get("firstName"), map.get("lastName"), map.get("emailAddress"), true, Role.ADMINISTRATOR);
            //employee.setId(Long.parseLong(map.get("id")));
            return Optional.of(employee);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
