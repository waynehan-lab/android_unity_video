using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;
using System.IO;
using UnityEngine.EventSystems;

public class Main : MonoBehaviour
{

    private AndroidJavaObject mUnityAcvity;

    // Use this for initialization
    void Start()
    {
        mUnityAcvity = new AndroidJavaClass("com.unity3d.player.UnityPlayer").GetStatic<AndroidJavaObject>("currentActivity");
        mUnityAcvity.Call("initSurface");
    }

    // Update is called once per frame
    void Update()
    {

    }

    public void UpdateTexImage()
    {
        mUnityAcvity.Call("updateTexImage");
    }

}
