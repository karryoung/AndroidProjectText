// IMyAidlInterface.aidl
package com.li.androidprojecttext;

// Declare any non-default types here with import statements
//客户端和服务端使用的是相同的
interface IMyAidlInterface {

void bindSuccess();

   void unbind();
}
