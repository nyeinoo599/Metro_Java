/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */ 
package controller;

import java.sql.ResultSet;
import database.DB_connection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pos.POS;




/**
 * FXML Controller class
 *
 * @author MITUSER-2
 */
public class LoginFxmlController implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private TextField txtUsername;

    @FXML
    private RadioButton rdAdmin;

    @FXML
    private ToggleGroup rdusertype;

    @FXML
    private RadioButton rdStaff;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnLogin;
    
   
    Parent root;
    Connection con;
    
    public static String username;
   
    @FXML
    void handleLoginAction() {
        
 

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        DB_connection db = new DB_connection();
        
        try{
        con = db.getConnection();
        }
        catch(ClassNotFoundException ex){
            System.out.println("Connection error");
        }
    }


@FXML
private void handleLoginAction(ActionEvent event) throws SQLException, IOException{
if (txtUsername.getText().isEmpty())
    JOptionPane.showMessageDialog(null,"Please fill usernamae");
else if (txtPassword.getText().isEmpty())
    JOptionPane.showMessageDialog(null,"Please fill password");
else if(!rdAdmin.isSelected()&& !  rdStaff.isSelected())
    JOptionPane.showMessageDialog(null, "Please choose your type");
else{
    String userType="";
    if(rdStaff.isSelected())
        userType="staff";
    else
        userType="admin";
    String sql="select * from users where name=? and password =? and user_type=?";
    PreparedStatement pst = con.prepareStatement(sql);
    pst.setString(1, txtUsername.getText());
    pst.setString(2, txtPassword.getText());
    pst.setString(3, userType);
    ResultSet rs = pst.executeQuery();
    
    if(rs.next()){
        Stage stage=POS.stage;
        username = rs.getString("name");
        if(userType.equals("staff"))
            root = FXMLLoader.load(getClass().getResource("/view/Staff.fxml"));
        else 
             root = FXMLLoader.load(getClass().getResource("/view/Admin.fxml"));
        
               Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
}
    }
}

}