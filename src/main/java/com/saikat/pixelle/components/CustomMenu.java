package com.saikat.pixelle.components;

import com.saikat.pixelle.listeners.OnMenuItemClickListener;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.List;
import java.util.Map;


public class CustomMenu extends MenuBar {

    private OnMenuItemClickListener onMenuItemClickListener;

    public CustomMenu() {
        Menu fileMenu = new Menu("File");
        MenuItem newFile = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem exit = new MenuItem("Exit");

        fileMenu.getItems().addAll(newFile, open, new SeparatorMenuItem(), exit);

        Menu editMenu = new javafx.scene.control.Menu("Edit");
        editMenu.getItems().addAll(new MenuItem("Cut"), new MenuItem("Copy"), new MenuItem("Paste"));

        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");

        helpMenu.getItems().add(about);

        this.getMenus().addAll(fileMenu, editMenu, helpMenu);
    }

    public CustomMenu(Map<String, List<String>> menus, OnMenuItemClickListener listener ){
        this.onMenuItemClickListener = listener;
        this.setMenus(menus);
    }

    public CustomMenu(Map<String, List<String>> menus ){
        this(menus, null);
    }

    public void setMenus(Map<String, List<String>> menus){
        this.getMenus().clear();

        int menuIndex = 0;
        for(Map.Entry<String, List<String>> entry : menus.entrySet()){
            int itemIndex = 0;

            Menu menu = new Menu(entry.getKey());
            for( String item:  entry.getValue() ){
                MenuItem menuItem = new MenuItem(item);
                menu.getItems().add(menuItem);

                // had problem accessing variable inside listener
                Integer finalMenuIndex = menuIndex;
                Integer finalItemIndex = itemIndex;
                menuItem.setOnAction(action -> {
                    if ( onMenuItemClickListener != null )  onMenuItemClickListener.onClick(finalMenuIndex, finalItemIndex);
                });

                itemIndex++;
            }
            this.getMenus().add(menu);
            menu.getItems().add(new SeparatorMenuItem());

            menuIndex++;
        }
    }

    public void addMenu(Menu menu) {
        this.getMenus().add(menu);
    }

    public void addMenuItems(String menuTitle, List<String> menuItems){
        Menu menu = new Menu(menuTitle);
        for( String item : menuItems ){
            menu.getItems().add(new MenuItem(item));
        }
        this.addMenu(menu);
    }

    public void addMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }
}
