// IMyAidlInterface.aidl
package com.ljy.ljyutils;
import com.ljy.ljyutils.bean.Book;

interface IMyAidlInterface {
    void callByService();
    void addBookIn(in Book bean);
    void addBookout(out Book bean);
    void addBookInout(inout Book bean);
    List<Book> getBooks();
}
