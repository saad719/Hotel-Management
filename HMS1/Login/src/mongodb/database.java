package mongodb;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import java.util.Vector;
//import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.UpdateResult;
//import com.mycompany.mavenproject1.FrontendGUI.Comment;
//import com.mycompany.mavenproject1.FrontendGUI.PostPanel;
//import com.mycompany.mavenproject1.FrontendGUI.PostPanel.*;
//import com.mycompany.mavenproject1.Leaderboard.*;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.bson.Document;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.FindIterable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import com.mongodb.client.FindIterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mongodb.database.Room;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 *
 * @author apple
 */
public class database {
    public MongoDatabase database;
    
    static database obj = new database();
    private database()
    {
        start();
    }
    
    public void start() 
    {
        // Start code here, similar to Start() in Unity
        Logger.getLogger( "org.mongodb.driver" ).setLevel(Level.WARNING);
    // TODO:
    //  Replace the placeholder connection string below with your
    // Altas cluster specifics. Be sure it includes
    // a valid username and password! Note that in a production environment,
    // you do not want to store your password in plain-text here.
    ConnectionString mongoUri = new ConnectionString("mongodb+srv://saadnm2148:batman12@cluster0.k77srwp.mongodb.net/");

    // Provide the name of the database and collection you want to use.
    // If they don't already exist, the driver and Atlas will create them
    // automatically when you first write data.
    String dbName = "hms";
    String collectionName = "Users";
    String collectionName2 = "Reservations";
    String collectionName3 = "orders_restaurant";
     String collectionName4 = "Rooms";
    // a CodecRegistry tells the Driver how to move data between Java POJOs (Plain Old Java Objects) and MongoDB documents
    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    // The MongoClient defines the connection to our MongoDB datastore instance (Atlas) using MongoClientSettings
    // You can create a MongoClientSettings with a Builder to configure codecRegistries, connection strings, and more
    MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .applyConnectionString(mongoUri).build();

    MongoClient mongoClient = null;
    try {
       mongoClient = MongoClients.create(settings);
    } catch (MongoException me) {
      System.err.println("Unable to connect to the MongoDB instance due to an error: " + me);
      System.exit(1);
    }

    // MongoDatabase defines a connection to a specific MongoDB database
     database = mongoClient.getDatabase(dbName);
    // MongoCollection defines a connection to a specific collection of documents in a specific database
    MongoCollection<Userdata> collection = database.getCollection(collectionName, Userdata.class);
    MongoCollection<roomreservation> collection2 = database.getCollection(collectionName2, roomreservation.class);
    MongoCollection<restaurantHandler> collection3 = database.getCollection(collectionName3, restaurantHandler.class);
    MongoCollection<Room> collection4 = database.getCollection(collectionName4, Room.class);
   
    }
    public static class Room
    {
        public String room_number;
        public String room_status;
        
     public Room(){
        
    }
    public Room(String room_number)
    {
        this.room_number= room_number;
        room_status= "Pending";
        
    }
     public String getRoomNumber() {
        return room_number;
    }

    public void setRoomNumber(String roomNumber) {
        this.room_number = roomNumber;
    }

    public String getStatus() {
        return room_status;
    }

    public void setStatus(String status) {
        this.room_status = status;
    }
        
        
    }
   
    public void insertpendingroom(String room_number)   
     {
          Room pending_room = new Room(room_number);
          MongoCollection<Room> collection4 = database.getCollection("Rooms", Room.class);
          collection4.insertOne(pending_room);
         
     }
      public void displaypendingrooms() {
        // Assuming you have a collection named "orders_restaurant"
        MongoCollection<Room> collection4 = database.getCollection("Rooms", Room.class);

        // Fetch all documents from the collection
        FindIterable<Room> orders = collection4.find();

        // Prepare data for the table
        List<Room> data3 = new ArrayList<>();
        for (Room order : orders) {
            data3.add(order);
        }

        // Define column names
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Room Number");
        columnNames.add("Status");
        // Add more columns as needed based on your document structure

        // Create a JTable with the fetched data
        JTable table = new JTable(buildTableModel3(data3, columnNames));

        // Optionally, you can embed the JTable in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Display the table in a JFrame or another suitable container
        JFrame frame = new JFrame("Pending Rooms");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }

    // Helper method to convert the list of restaurantHandler objects into a TableModel
    private DefaultTableModel buildTableModel3(List<Room> data3, Vector<String> columnNames) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Room order : data3) {
            Vector<Object> row = new Vector<>();
            row.add(order.getRoomNumber());
            row.add(order.getStatus());
            // Add more columns as needed based on your restaurantHandler class
            model.addRow(row);
        }

        return model;
    }
     public Room Checkroomexit(Room num) {
    MongoCollection<Room> collection4 = database.getCollection("Rooms", Room.class);
    
    // Assuming Userdata has a field called 'username' that you want to use for the query
    Bson filter = Filters.eq("room_number", num.room_number);

    // Find the user based on the username
    return collection4.find(filter).first();
}
      public void removepending(Room num) {
        MongoCollection<Room> collection4 = database.getCollection("Rooms", Room.class);

        // Create a filter to identify the reservation to delete based on room number
        Bson filter =Filters.eq("room_number", num.room_number);
        // Perform the deletion
        collection4.deleteOne(filter);

       
    } 
      
    
// 
       
    public static class restaurantHandler
    {
       public String name;
       public String price;
       
       public restaurantHandler(String name, String price)
       {
           this.name = name;
           this.price = price;
       }
        public String getItemName() {
        return name;
    }
         public restaurantHandler() {
        // Initialize any necessary fields
    }

    public String getPrice() {
        return price;
    }
        
    }
        public static database getInstance()
    {
        return obj;
    }
     
         public void addToOrder(restaurantHandler orderItem) {
        // Assuming you have a collection for orders named "Orders"
        MongoCollection<restaurantHandler> collection3 = database.getCollection("orders_restaurant", restaurantHandler.class);

        // Create a document representing the order item
//        Document orderItem = new Document()
//                .append("itemName", itemName)
//                .append("quantity", quantity);
        // Insert the order item into the "Orders" collection
        collection3.insertOne(orderItem);

        // Display a message or update the UI to indicate the item was added to the order
        JOptionPane.showMessageDialog(null, "Item added to the order", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
         
         public void paymentisconfirmed()
         {
             MongoCollection<restaurantHandler> collection3 = database.getCollection("orders_restaurant", restaurantHandler.class);

        // Delete all documents from the collection
             collection3.deleteMany(new Document());   
             JOptionPane.showMessageDialog(null, "Payment Completed Succesfully", "Restaurant Invoice Is Cleared", JOptionPane.INFORMATION_MESSAGE);
             
         }
         
           public void displayOrders() {
        // Assuming you have a collection named "orders_restaurant"
        MongoCollection<restaurantHandler> collection3 = database.getCollection("orders_restaurant", restaurantHandler.class);

        // Fetch all documents from the collection
        FindIterable<restaurantHandler> orders = collection3.find();

        // Prepare data for the table
        List<restaurantHandler> data = new ArrayList<>();
        for (restaurantHandler order : orders) {
            data.add(order);
        }

        // Define column names
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Item Name");
        columnNames.add("Price");
        // Add more columns as needed based on your document structure

        // Create a JTable with the fetched data
        JTable table = new JTable(buildTableModel(data, columnNames));

        // Optionally, you can embed the JTable in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Display the table in a JFrame or another suitable container
        JFrame frame = new JFrame("Restaurant Invoice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }

    // Helper method to convert the list of restaurantHandler objects into a TableModel
    private DefaultTableModel buildTableModel(List<restaurantHandler> data, Vector<String> columnNames) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (restaurantHandler order : data) {
            Vector<Object> row = new Vector<>();
            row.add(order.getItemName());
            row.add(order.getPrice());
            // Add more columns as needed based on your restaurantHandler class
            model.addRow(row);
        }

        return model;
    }
      
    
    
    
    
    
    public static class roomreservation
    {
    public String Name;
    public String roomnumber;
    public String roomtype;
    public String pricee;
    public String email;
    public String cnic;
    public String nationality;
    public String Address;
    public String mobile;
    public String gender;
    public Date checkin;
    public Date checkout;
    
    public roomreservation(String Name, String roomnumber, String roomtype, String pricee,String email, String cnic, String nationality,String Address,String mobile, String gender,Date checkin,Date checkout) 
    {
    //  this.name = name;
      this.Name = Name;
      this.roomnumber = roomnumber;
      this.roomtype = roomtype;
      this.pricee = pricee;
      this.email = email;
        this.cnic = cnic;
        this.nationality = nationality;
        this.Address = Address;
        this.mobile = mobile;
        this.gender = gender;
        this.checkin = new Date(checkin.getTime());  // Create a new Date object to avoid reference issues
        this.checkout = new Date(checkout.getTime()); 
    }
    public roomreservation()
    {
        
    }
    
    public String getname()
    {
        return Name;
    }
     public String getprice()
    {
        return pricee;
    }
    
    
    
    }
//     public static database getInstance()
//    {
//        return obj;
//    }
    
     public void insertreservation(roomreservation reserve)
     {
          MongoCollection<roomreservation> collection2 = database.getCollection("Reservations", roomreservation.class);
          collection2.insertOne(reserve);
         
     }
      public roomreservation getuserfrom_db(roomreservation data) {
    
     MongoCollection<roomreservation> collection2 = database.getCollection("Reservations", roomreservation.class);
    // Assuming Userdata has a field called 'username' that you want to use for the query
    Bson filter = Filters.eq("roomnumber", data.roomnumber);

    // Find the user based on the username
    return collection2.find(filter).first();
      }
     
      
    // Other fields and methods...

    // Function to modify values in the existing database
    public void updateReservation(roomreservation newData) {
        MongoCollection<roomreservation> collection2 = database.getCollection("Reservations", roomreservation.class);

        // Identify the reservation to update based on room number
        Bson filter = Filters.eq("roomnumber", newData.roomnumber);

        // Create an update document with the new data
        Document updateDocument = new Document("$set", new Document()
                .append("Name", newData.Name)
                .append("roomtype", newData.roomtype)
                .append("pricee", newData.pricee)
                .append("email", newData.email)
                .append("cnic", newData.cnic)
                .append("nationality", newData.nationality)
                .append("Address", newData.Address)
                .append("mobile", newData.mobile)
                .append("gender", newData.gender)
                .append("checkin", newData.checkin)
                .append("checkout", newData.checkout));

        // Perform the update
        collection2.updateOne(filter, updateDocument);

        System.out.println("Reservation updated successfully.");
    }
      public void deleteReservation(String roomNumber, String Name) {
        MongoCollection<roomreservation> collection2 = database.getCollection("Reservations", roomreservation.class);

        // Create a filter to identify the reservation to delete based on room number
        Bson filter = Filters.and(
        Filters.eq("roomnumber", roomNumber), Filters.eq("Name", Name));
        // Perform the deletion
        collection2.deleteOne(filter);

       
    } 
      public void displayInvoice(String Name) {
        // Assuming you have a collection named "orders_restaurant"
        MongoCollection<roomreservation> collection2 = database.getCollection("Reservations", roomreservation.class);

        // Fetch all documents from the collection
        Bson filter = eq("Name", Name);
        FindIterable<roomreservation> reservations = collection2.find(filter);

        // Prepare data for the table
        List<roomreservation> data2 = new ArrayList<>();
        for (roomreservation checkin : reservations) {
            data2.add(checkin);
        }

        // Define column names
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Guest Name");
        columnNames.add("Payment Due");
        // Add more columns as needed based on your document structure

        // Create a JTable with the fetched data
        JTable table = new JTable(buildTableModel2(data2, columnNames));

        // Optionally, you can embed the JTable in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Display the table in a JFrame or another suitable container
        JFrame frame = new JFrame("Receptionist Invoice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }

    // Helper method to convert the list of restaurantHandler objects into a TableModel
    private DefaultTableModel buildTableModel2(List<roomreservation> data2, Vector<String> columnNames) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (roomreservation order : data2) {
            Vector<Object> row = new Vector<>();
            row.add(order.getname());
            row.add(order.getprice());
            // Add more columns as needed based on your restaurantHandler class
            model.addRow(row);
        }

        return model;
    }


     
     
     
    
    
    
    
    public static class Userdata 
    {
   // public String name;
    public String username;
    //public String role;   
    //public String email;
    public String password;
    
    public Userdata()
    {
        username = null ;
    }
    
    public Userdata(String username, String password) 
    {
    //  this.name = name;
      this.username = username;
     // this.role = role;
     // this.email = email;
      this.password = password;

      
    }

    // empty constructor required when we fetch data from the database -- getters and setters are later used to
    // set values for member variables
    

    

//   public String getName() {
//      return name;
//    }
//
//    public void setName(String name) {
//      this.name = name;
//    }
//
//    
  }
    public void insertuserdataindb(Userdata data)
    {
        MongoCollection<Userdata> collection = database.getCollection("Users", Userdata.class);
        collection.insertOne(data);
    }
 

   public Userdata getuserfromdb(Userdata data) {
    MongoCollection<Userdata> collection = database.getCollection("Users", Userdata.class);
    
    // Assuming Userdata has a field called 'username' that you want to use for the query
    Bson filter = Filters.eq("username", data.username);

    // Find the user based on the username
    return collection.find(filter).first();
}
    }