/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import database.DB_connection;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Product;
import pos.POS;

/**
 * FXML Controller class
 *
 * @author MITUSER-2
 */
public class VoucherController implements Initializable {

    @FXML
    private AnchorPane vPane;
    @FXML
    private TableView<?> tableView;
    @FXML
    private TableColumn<?, ?> vName;
    @FXML
    private TableColumn<?, ?> vSize;
    @FXML
    private TableColumn<?, ?> vPrice;
    @FXML
    private TableColumn<?, ?> vQty;
    @FXML
    private TableColumn<?, ?> vAmount;
    @FXML
    private Label lblSlip;
    @FXML
    private Label lblDate;
    @FXML
    private Button vPrint;
    @FXML
    private Button vBack;
    @FXML
    private Label vTotal;
    @FXML
    private Label vReceive;
    @FXML
    private Label vchange;
    private int slip_no;
    
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    ObservableList<Product> productList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DB_connection db = new DB_connection();
        
        try{
        
        vSize.setCellValueFactory(new PropertyValueFactory("size"));
        vName.setCellValueFactory(new PropertyValueFactory("name"));
        vPrice.setCellValueFactory(new PropertyValueFactory("price"));
        vQty.setCellValueFactory(new PropertyValueFactory("qty"));
            
        con = db.getConnection();
        LocalDate date = LocalDate.now();
        lblDate.setText(date.toString());
        int slip_id = StaffController.sid;
        lblSlip.setText("00"+slip_id);
        String sql = "";
        }
        catch(ClassNotFoundException ex){
            System.out.println("Connection error");
        }
    }    

    @FXML
    private void handlePrintAction(ActionEvent event) {
    }

    @FXML
    private void handleBackAction(ActionEvent event) throws IOException {
        
        Stage stage=POS.stage;
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginFxml.fxml"));
        Scene scene = new Scene(root);
               
        stage.setScene(scene);
        stage.show();
    }
    
    private void initSaleList() throws SQLException{
        productList = FXCollections.observableArrayList();
        String sql = "Select p.*, s.qty from products p,sales_details where s.product_id=p.product_id and s.sale_id=? ";
        pst = con.prepareStatement(sql);
        pst.setInt(1, slip_no);
        rs = pst.executeQuery();
        
        while(rs.next()){
            productList.add(new Product(rs.getInt("qty"), rs.getInt("price")*rs.getInt("qty"),
                    rs.getInt("price"), rs.getString("name"), rs.getString("size")));
        }
    }
    
}
