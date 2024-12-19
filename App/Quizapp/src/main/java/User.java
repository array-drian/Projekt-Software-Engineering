import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class User {
    private String name = "Max Mustermann'";

    public User() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Method to save the User to the database
    public void saveToDatabase() {
        String query = "INSERT INTO User (name) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, this.name);
            preparedStatement.executeUpdate();
            System.out.println("User saved: " + this.name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
