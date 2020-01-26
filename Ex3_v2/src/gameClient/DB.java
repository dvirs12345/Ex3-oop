package gameClient;

import java.sql.*;

public class DB {
    public static final String jdbcUrl = "jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
    public static final String jdbcUser = "student";
    public static final String jdbcUserPassword = "OOP2020student";


    /**
     * @param id - user id.
     * @return The number of games played by the user.
     */
    public static int numOfGames(int id) {
        int ind = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection =
                    DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            String allCustomersQuery = "SELECT * FROM Logs where userID=" + id;

            ResultSet resultSet = statement.executeQuery(allCustomersQuery);
            while (resultSet.next()) {
                System.out.println(ind + ") Id: " + resultSet.getInt("UserID") + ", level: " + resultSet.getInt("levelID") + ", score: " + resultSet.getInt("score") + ", moves: " + resultSet.getInt("moves") + ", time: " + resultSet.getDate("time"));
                ind++;
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(ind);
        return ind;
    }

    public static int currentLevel(int id) {
        int ind = 0;
        int level = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection =
                    DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            String allCustomersQuery = "SELECT * FROM Logs where userID=" + id;

            ResultSet resultSet = statement.executeQuery(allCustomersQuery);
            while (resultSet.next()) {
                System.out.println(ind + ") Id: " + resultSet.getInt("UserID") + ", level: " + resultSet.getInt("levelID") + ", score: " + resultSet.getInt("score") + ", moves: " + resultSet.getInt("moves") + ", time: " + resultSet.getDate("time"));
                ind++;
                if (resultSet.getInt("levelID") > level) {
                    level = resultSet.getInt("levelID");
                }
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(ind);
        return level;
    }

    public static int bestScore(int id, int level) {
        int ind = 0;
        int score = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection =
                    DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            String allCustomersQuery = "SELECT * FROM Logs where userID=" + id;

            ResultSet resultSet = statement.executeQuery(allCustomersQuery);
            while (resultSet.next()) {
                System.out.println(ind + ") Id: " + resultSet.getInt("UserID") + ", level: " + resultSet.getInt("levelID") + ", score: " + resultSet.getInt("score") + ", moves: " + resultSet.getInt("moves") + ", time: " + resultSet.getDate("time"));
                ind++;
                if (resultSet.getInt("levelID") == level) {
                    if (resultSet.getInt("score") > score) {
                        score = resultSet.getInt("score");
                    }
                }
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(ind);
        return score;

    }


    public static int Position(int id, int level) {
        int ind = 0;
        int score = 0;
        int position = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection =
                    DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            String allCustomersQuery = "SELECT * FROM Logs;";

            ResultSet resultSet = statement.executeQuery(allCustomersQuery);
            while (resultSet.next()) {
                System.out.println(ind + ") Id: " + resultSet.getInt("UserID") + ", level: " + resultSet.getInt("levelID") + ", score: " + resultSet.getInt("score") + ", moves: " + resultSet.getInt("moves") + ", time: " + resultSet.getDate("time"));
                ind++;
                if (resultSet.getInt("levelID") == level) {
                    if (resultSet.getInt("UserID") == id) {
                        if (resultSet.getInt("score") > score) {
                            score = resultSet.getInt("score");
                            position = ind;

                        }
                    }
                }
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(ind);
        return position;

    }

    public static void main(String[] args) {
        int id = 322438938;
        numOfGames(id);
        System.out.println("Current Level: ");
        System.out.println(currentLevel(id));
    }

}
