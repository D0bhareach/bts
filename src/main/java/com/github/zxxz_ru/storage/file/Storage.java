package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.entity.StoreUnit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage<T> {
    enum Mode {USER, TASK, PROJECT};
    private final String path;

    public Storage(String p){this.path = p;}

    public void store(List<T> list){
        final String storageDir = "~/opt/storage/epam/";
        final String userFile = storageDir+"users";
        final String taskFile = storageDir+"tasks";
        final String projectFile = storageDir+"projects";
        try(
            FileOutputStream fileOut =
                    new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            ){
         out.writeObject(list);
      }  catch (IOException i) {
         i.printStackTrace();
      }
    }

    @SuppressWarnings("unchecked")
    public List<T> get(Mode mode){
        List<T> list = new ArrayList<>();
        try (
                FileInputStream fileIn = new FileInputStream(path);
                ObjectInputStream in = new ObjectInputStream(fileIn)){
             list = (List<T>) in.readObject();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
        }
        return list;
    }
}
