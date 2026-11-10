package databases.part03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databases.part02.Artist;

/**
 * Data Access Object for the Album table in the Chinook database.
 */
public class AlbumDAO {

    /**
     * The connection string used to connect to the database. You MUST use this
     * string when connecting to the database using JDBC. In the unit tests, this
     * field will be set to a different value.
     */
    private final String connectionString;

    /**
     * Creates a new AlbumDAO that uses the specified connection string to connect
     * to the database. For example: "jdbc:sqlite:data/Chinook_Sqlite.sqlite"
     *
     * @param jdbcConnection see https://www.baeldung.com/java-jdbc-url-format
     */
    public AlbumDAO(String jdbcConnection) {
        this.connectionString = jdbcConnection;
    }

    /**
     * Returns a list of all albums that have the specified artist as the artist.
     * If there are no albums for the specified artist, the list is empty.
     *
     * @param artist the artist whose albums to retrieve.
     * @return a list of all albums that have the specified artist as the artist,
     *         sorted by AlbumId in ascending order.
     */
    public List<Album> getAlbumsByArtist(Artist artist) {

        List<Album> albumit = new ArrayList<>();

        String valmisLause = "SELECT * FROM Album WHERE ArtistId = ? ORDER BY AlbumId ASC";

        try (Connection connection = DriverManager.getConnection(connectionString);
            PreparedStatement statement = connection.prepareStatement(valmisLause)) {

            statement.setLong(1, artist.getId());

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                long albumId = result.getLong("AlbumId");
                String title = result.getString("Title");
                long artistId = result.getLong("ArtistId");

                albumit.add(new Album(albumId, title, artistId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return albumit;
    }

    /**
     * Adds the specified album to the database. Returns true if the album was
     * added successfully, false otherwise.
     *
     * @param album the album to add to the database.
     * @return true if the album was added successfully, false otherwise.
     */
    public boolean addAlbum(Album album) {

            String valmisLause = "INSERT INTO Album (Title, ArtistId) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionString);
            PreparedStatement statement = connection.prepareStatement(valmisLause)) {

            statement.setString(1, album.getTitle());
            statement.setLong(2, album.getArtistId());

        return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates the specified album in the database. Returns true if the album was
     * updated successfully, false otherwise.
     *
     * @param album the album to update in the database.
     * @return true if the album was updated successfully, false otherwise.
     */
    public boolean updateAlbum(Album album) {

        String valmisLause = "UPDATE Album SET Title = ?, ArtistId = ? WHERE AlbumId = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
            PreparedStatement statement = connection.prepareStatement(valmisLause)) {

            statement.setString(1, album.getTitle());
            statement.setLong(2, album.getArtistId());
            statement.setLong(3, album.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes the specified album from the database. Returns true if the album was
     * deleted successfully, false otherwise.
     *
     * @param album the album to delete from the database.
     * @return true if the album was deleted successfully, false otherwise.
     */
    public boolean deleteAlbum(Album album) {
    
        String valmisLause = "DELETE FROM Album WHERE AlbumId = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
            PreparedStatement statement = connection.prepareStatement(valmisLause)) {

            statement.setLong(1, album.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
