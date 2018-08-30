package com.dp.uheadmaster.interfaces;

import com.dp.uheadmaster.models.Content;
import com.dp.uheadmaster.models.QuizAnswer;

import java.util.ArrayList;

/**
 * Created by DELL on 15/10/2017.
 */

public interface VideoLinksInterface {

    public void getVideoName(String name, ArrayList<Content>contents , int position,String lectureName,int type);
}
