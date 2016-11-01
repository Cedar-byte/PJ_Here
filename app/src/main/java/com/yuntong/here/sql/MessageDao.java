package com.yuntong.here.sql;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.yuntong.here.entity.MessageData;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class MessageDao {
    private Context context;
    private Dao<MessageData, Integer> userDaoOpe;
    private DatabaseHelper helper;

    public MessageDao(Context context)
    {
        this.context = context;
        try
        {
            helper = DatabaseHelper.getHelper(context);
            userDaoOpe = helper.getDao(MessageData.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     * @param message
     */
    public void add(MessageData message)
    {
        try
        {
            userDaoOpe.create(message);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public List<MessageData>  getAll(String userId)
    {
        try
        {
             return userDaoOpe.queryBuilder().where().eq("yuntongid", userId)
                .query();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }



    public  void deleteall(){
        userDaoOpe.clearObjectCache();
    }

    public void deleteitem(String adviceId,String userid){
        try {
            DeleteBuilder<MessageData,Integer> deleteBuilder=userDaoOpe.deleteBuilder();
            deleteBuilder.where().eq("adviceId",adviceId).and().eq("yuntongid", userid);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updata(String userId,String userid,String valua,String type){
        try {
            UpdateBuilder<MessageData,Integer> updateBuilder=userDaoOpe.updateBuilder();
            updateBuilder.updateColumnExpression("isClick",valua);
            updateBuilder.where().eq("userId",userId).and().eq("yuntongid",userid).and().eq("type",type);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
