package com.example.fta.Models;

import android.content.Context;
import android.widget.Toast;

import com.example.fta.Interface.ChangeNumberItem;
import com.example.fta.TinyDB;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void clearListCart(){
         tinyDB.removeString("Card List");
    }

    public void insertFood(Food item){
        ArrayList<Food> listFoods = getListCart();
        boolean exist = false;
        int n = 0;
        for (int i = 0; i < listFoods.size(); i++){
            if (listFoods.get(i).getTitle().equals(item.getTitle())){
                exist = true;
                n = i;
                break;
            }
        }

        if (exist){
            listFoods.get(n).setNumberInCart(item.getNumberInCart());
        }else {
            listFoods.add(item);
        }

        tinyDB.putListObject("Card List", listFoods);
        Toast.makeText(context, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Food> getListCart(){
        return tinyDB.getListObject("Card List");
    }

    public void minNumberFood(ArrayList<Food> listfood, int position, ChangeNumberItem changeNumberItem){
        if (listfood.get(position).getNumberInCart()==1){
            listfood.remove(position);
        }else {
            listfood.get(position).setNumberInCart(listfood.get(position).getNumberInCart()-1);
        }
        tinyDB.putListObject("Card List", listfood);
        changeNumberItem.changed();
    }

    public void maxNumberFood(ArrayList<Food> listfood, int position, ChangeNumberItem changeNumberItem){
        listfood.get(position).setNumberInCart(listfood.get(position).getNumberInCart()+1);
        tinyDB.putListObject("Card List", listfood);
        changeNumberItem.changed();
    }

    public int getTotalFee(){
        ArrayList<Food> listfoods = getListCart();
        int fee = 0;
        for (int i = 0; i < listfoods.size(); i++){
            fee=fee+(listfoods.get(i).getFee()*listfoods.get(i).getNumberInCart());
        }
        return fee;
    }
}
