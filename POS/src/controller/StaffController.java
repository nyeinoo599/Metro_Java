/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;


import model.Product;
import com.mysql.cj.protocol.Resultset;
import database.DB_connection;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import pos.POS;

/**
 * FXML Controller class
 *
 * @author MITUSER-2
 */
public class StaffController implements Initializable {

    @FXML
    private TableView<Product> tb_products;
    @FXML
    private TableColumn<?, ?> colProdctid;
    @FXML
    private TableColumn<?, ?> colCategory;
    @FXML
    private TableColumn<?, ?> colBrand;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colSize;
    @FXML
    private TableColumn<?, ?> colPrice;
    @FXML
    private TableColumn<?, ?> colStock;
    @FXML
    private TableColumn<?, ?> image;
    @FXML
    private TextField txtsearch;
    @FXML
    private Label lblname;
    @FXML
    private Label lbldate;
    @FXML
    private Label lblslip;
    @FXML
    private TableView<Product> tbsales;
    @FXML
    private TableColumn<?, ?> colsProductid;
    @FXML
    private TableColumn<?, ?> colsBrand;
    @FXML
    private TableColumn<?, ?> colsName;
    @FXML
    private TableColumn<?, ?> colsSize;
    @FXML
    private TableColumn<?, ?> colsPrice;
    @FXML
    private TableColumn<?, ?> colsQty;
    @FXML
    private TableColumn<?, ?> colsAmount;
    @FXML
    private RadioButton rdCash;
    @FXML
    private ToggleGroup rdPayment;
    @FXML
    private TextField txtTotal;
    @FXML
    private TextField txtReceive;
    @FXML
    private RadioButton rdPay;
    @FXML
    private Button btnCart;
    @FXML
    private TextField txtChange;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnrefresh;
    private Button btnlogout;
    private Button btnpayment;
    @FXML
    private Button search;
    Connection con = null;
    int total;
    
    ObservableList<Product> productList,cartList;
    
    Statement st;
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cartList = FXCollections.observableArrayList();
        DB_connection db = new DB_connection();
        try{
            con = db.getConnection();
            lblname.setText(LoginFxmlController.username);
            LocalDate date = LocalDate.now();
            lbldate.setText(date.toString());
            countSlip();
            initProductList();
            
//            product list
            colProdctid.setCellValueFactory(new PropertyValueFactory("pid"));
            colCategory.setCellValueFactory(new PropertyValueFactory("category"));
            colBrand.setCellValueFactory(new PropertyValueFactory("brand"));
            colsName.setCellValueFactory(new PropertyValueFactory("name"));
            colSize.setCellValueFactory(new PropertyValueFactory("size"));
            colName.setCellValueFactory(new PropertyValueFactory("name"));
            colPrice.setCellValueFactory(new PropertyValueFactory("price"));
            colStock.setCellValueFactory(new PropertyValueFactory("stock"));
            image.setCellValueFactory(new PropertyValueFactory("imgView"));
            
            tb_products.setItems(productList);
            
//            insert value
            colsProductid.setCellValueFactory(new PropertyValueFactory("pid"));
            colsBrand.setCellValueFactory(new PropertyValueFactory("brand"));
            colsName.setCellValueFactory(new PropertyValueFactory("name"));
            colCategory.setCellValueFactory(new PropertyValueFactory("category"));
            colsSize.setCellValueFactory(new PropertyValueFactory("size"));
            colsPrice.setCellValueFactory(new PropertyValueFactory("price"));
            colsQty.setCellValueFactory(new PropertyValueFactory("qty"));
            colsAmount.setCellValueFactory(new PropertyValueFactory("amount"));
            
//            insert action
//            colsQty.setCellFactory(column->{
//                new TableCell<Product,Void>(){
//                    private Button btnPlus = new Button(" + ");
//                    private Button btnMinus = new Button(" - ");
//                    private Label lblQty = new Label("");
//                  
//                    
//                };
//                
//            });
            
           
            
//            initProductList();
        }catch(ClassNotFoundException ex){
            System.out.println("connection error");
        }catch(SQLException ex){
            
        }
    }    
    

    @FXML
    private void handleCartAction(ActionEvent event) throws SQLException {
        Product p = (Product)tb_products.getSelectionModel().getSelectedItem();
//       JOptionPane.showMessageDialog(null, p.getCategory());

//          
            if(p == null){
                JOptionPane.showMessageDialog(null, "Please select item from table");
            }else{
                String input = JOptionPane.showInputDialog("Enter quanity");
                String sql = "Insert into carts(product_id,qty) values(?,?)";
                pst = con.prepareStatement(sql);
                pst.setInt(1,p.getPid());
                pst.setInt(2, Integer.parseInt(input));
                pst.executeUpdate();
                int amount = p.getPrice()*Integer.parseInt(input);
                total+=amount;
               cartList.add(new Product(p.getPid(),p.getCategory(),Integer.parseInt(input),amount,
                       p.getBrand(),p.getPrice(),
                       p.getName(),p.getSize()));
                    tbsales.setItems(cartList);
                    txtTotal.setText(String.valueOf(total));
            }
         
    }
    
    @FXML
    void handlePaymentAction(ActionEvent event) {
        String payment = "";
        if(!rdCash.isSelected() && !rdPay.isSelected()){
            JOptionPane.showMessageDialog(null, "Please select payment type !!!");
        }else{
            if(rdPay.isSelected()){
                payment = "pay";
                txtChange.setText("0");
                txtReceive.setText(String.valueOf(total));
            }else{
                payment = "cash";
                if(txtReceive.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter receive amount");
                }else{
                    
                    int change = Integer.parseInt(txtReceive.getText())-Integer.parseInt(txtTotal.getText());
                    txtChange.setText(String.valueOf(change));
                }
            }
        }
    }
    
    @FXML
    void handleReceiveAction(MouseEvent event) {
        
    }

    @FXML
    private void handlePrintAction(ActionEvent event) throws SQLException {
    }
    
    @FXML
    private void handleTableAction(MouseEvent event){
       

        
    }
    
    @FXML
    private void handleLogoutAction(ActionEvent event) throws SQLException, IOException {
        
        Stage stage=POS.stage;
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginFxml.fxml"));
        Scene scene = new Scene(root);
               
        stage.setScene(scene);
                stage.show();
    }
    
    @FXML
    private void handleRefreshAction(ActionEvent event) throws SQLException{
        initProductList();
        tb_products.setItems(productList);
    }

    @FXML
    private void searchbtn(ActionEvent event) throws SQLException {
        if(txtsearch.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "Please enter seach product name !");
        else{
            String sql = "Select * from products where brand like ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, txtsearch.getText()+"%");
            rs = pst.executeQuery();
            boolean found = false;
            productList.removeAll(productList);
            while(rs.next()){
                found = true;
                productList.add(new Product(rs.getInt("product_id"),
                rs.getString("category"),rs.getString("brand"),
                rs.getString("size"),rs.getInt("price"),rs.getInt("stock_qty"),
                rs.getString("img_name")));
            }
            tb_products.setItems(productList);
            
            if(!found){
                initProductList();
                tb_products.setItems(productList);
                JOptionPane.showMessageDialog(null, "Search name is not found !");
            }
        }
    }
    
    
    
//    private void handlePrintAction(ActionEvent event){
//        
//    }
    
    public void initProductList() throws SQLException{
        productList = FXCollections.observableArrayList();
        String sql = "Select * from products";
         st = con.createStatement();
         rs = st.executeQuery(sql);
         System.out.println(rs);
        
        while(rs.next()){
            
        productList.add(new Product(rs.getInt("product_id"),
                rs.getString("category"),rs.getString("brand"),
                rs.getString("size"),rs.getInt("price"),rs.getInt("stock_qty"),
                rs.getString("img_name")));
        
        }
    }
    
    public void countSlip() throws SQLException{
        String sql = "Select count(*) AS count from sales";
        st = con.createStatement();
        rs = st.executeQuery(sql);
        if(rs.next()){
            int slip_no =rs.getInt("count")+1;
            lblslip.setText("00"+slip_no);
        }
    }
    
}

