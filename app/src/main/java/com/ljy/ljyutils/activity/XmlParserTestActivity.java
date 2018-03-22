package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Xml;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyLogUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XmlParserTestActivity extends BaseActivity {

    @BindView(R.id.textViewXml)
    TextView mTextViewXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_parser_test);
        ButterKnife.bind(mActivity);
//        methodDOM();
//        methodSAX();
        methodPULL();
    }

    /**
     * PULL的解析方式与SAX解析类似，都是基于事件的模式。
     * PULL提供了开始元素和结束元素。当某个元素开始时，
     * 我们可以调用parser.nextText从XML文档中提取所有字符数据，
     * 与SAX不同的是，在PULL解析过程中触发相应的事件调用方法返回的
     * 是数字，且我们需要自己获取产生的事件然后做相应的操作，
     * 而不像SAX那样由处理器触发一种事件的方法从而执行代码。
     * 当解释到一个文档结束时，自动生成EndDocument事件
     * <p>
     * 优点：SAX的优点PULL都有，而且解析方法比SAX更加简单
     * 缺点：可拓展性差：无法对 XML 树内容结构进行任何修改
     * <p>
     * 使用情境:
     * 适用于需要处理大型 XML 文档、性能要求较高、
     * 不需要对解析文档进行修改且不需要对解析文档多次访问的场合
     */
    private void methodPULL() {
        try {
            List<Book> bookList = new ArrayList<>();
            Book book = null;

            InputStream stream = getAssets().open("books.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(stream, "utf-8");

            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                int eventType = parser.getEventType();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        switch (tagName) {
                            case "bookstore":
                                LjyLogUtil.i("bookstore");
                                break;
                            case "book":
                                if (book == null) {
                                    book = new Book();
                                }
                                book.setId(Integer.parseInt(parser.getAttributeValue(0)));
                                break;
                            case "name":
                                book.setName(parser.nextText());
                                break;
                            case "author":
                                book.setAuthor(parser.nextText());
                                break;
                            case "year":
                                book.setYear(Integer.parseInt(parser.nextText()));
                                break;
                            case "price":
                                book.setPrice(Integer.parseInt(parser.nextText()));
                                break;
                            case "language":
                                book.setLanguage(parser.nextText());
                                break;
                            default:
                                LjyLogUtil.i("tagName:" + tagName);
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (book != null) {
                            bookList.add(book);
                            book = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                parser.next();
            }

            LjyLogUtil.i("bookList:" + bookList.toString());

            mTextViewXml.append("PULL \n 一共有" + bookList.size() + "本书:\n");
            mTextViewXml.append("----------------------\n");
            for (Book b : bookList) {
                mTextViewXml.append(String.format("\t%s=%s:\n", "id", b.getId()));
                mTextViewXml.append(String.format("\t%s=%s:\n", "name", b.getName()));
                mTextViewXml.append(String.format("\t%s=%s:\n", "author", b.getAuthor()));
                mTextViewXml.append(String.format("\t%s=%s:\n", "year", b.getYear()));
                mTextViewXml.append(String.format("\t%s=%s:\n", "language", b.getLanguage()));
                mTextViewXml.append(String.format("\t%s=%s:\n", "price", b.getPrice()));
                mTextViewXml.append("----------------------\n");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

    /**
     * SAX的全称是Simple APIs for XML，也即XML简单应用程序接口。
     * 与DOM不同，SAX提供的访问模式是一种顺序模式，这是一种快速读写XML数据的方式。
     * 当使用SAX分析器对XML文档进行分析时，会触发一系列事件，并激活相应的事件处理函数，
     * 应用程序通过这些事件处理函数实现对XML文档的访问，因而SAX接口也被称作事件驱动接口。
     * <p>
     * 优点：
     * 1、采用事件驱动模式，对内存耗费比较小。
     * 2、适用于只处理XML文件中的数据时。
     * 缺点：
     * 1、编码比较麻烦。
     * 2、很难同时访问XML文件中的多处不同数据。
     * <p>
     * 使用情境
     * 适用于需要处理大型 XML 文档、性能要求较高、
     * 不需要对解析文档进行修改且不需要对解析文档多次访问的场合
     */
    private void methodSAX() {
        try {
            InputStream stream = getAssets().open("books.xml");
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            SAXParserHandler handler = new SAXParserHandler();
            parser.parse(stream, handler);
            mTextViewXml.append("一共有" + handler.getBookList().size() + "本书:\n");
            mTextViewXml.append("----------------------\n");
            for (Book book : handler.getBookList()) {
                mTextViewXml.append(String.format("\t%s=%s:\n", "id", book.getId()));
                mTextViewXml.append(String.format("\t%s=%s:\n", "name", book.getName()));
                mTextViewXml.append(String.format("\t%s=%s:\n", "author", book.getAuthor()));
                mTextViewXml.append(String.format("\t%s=%s:\n", "year", book.getYear()));
                mTextViewXml.append(String.format("\t%s=%s:\n", "language", book.getLanguage()));
                mTextViewXml.append(String.format("\t%s=%s:\n", "price", book.getPrice()));
                mTextViewXml.append("----------------------\n");
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class SAXParserHandler extends DefaultHandler {

        private String value = null;
        private Book book = null;
        private int bookIndex = 0;
        private List<Book> bookList = new ArrayList<>();

        public List<Book> getBookList() {
            return bookList;
        }

        //解析开始
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        //解析结束
        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        //解析xml元素
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (qName.equals("book")) {
                bookIndex++;
                book = new Book();
                int num = attributes.getLength();
                for (int i = 0; i < num; i++) {
                    if (attributes.getQName(i).equals("id")) {
                        book.setId(Integer.parseInt(attributes.getValue(i)));
                    }
                }
            } else {
                LjyLogUtil.i("节点名是：" + qName);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            //判断是否针对一本书已经遍历结束
            switch (qName) {
                case "book":
                    bookList.add(book);
                    book = null;
                    break;
                case "name":
                    book.setName(value);
                    break;
                case "author":
                    book.setAuthor(value);
                    break;
                case "year":
                    book.setYear(Integer.parseInt(value));
                    break;
                case "price":
                    book.setPrice(Integer.parseInt(value));
                    break;
                case "language":
                    book.setLanguage(value);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            value = new String(ch, start, length);
            if (!TextUtils.isEmpty(value.trim())) {
                LjyLogUtil.i("节点值是：" + value);
            }
        }
    }

    public static class Book {
        private int id;
        private String name;
        private String author;
        private String language;
        private int year;
        private int price;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "Book{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", author='" + author + '\'' +
                    ", language='" + language + '\'' +
                    ", year=" + year +
                    ", price=" + price +
                    '}';
        }
    }

    /**
     * 基于文档驱动方式 (Document Object Model)
     * DOM是基于树形结构的的节点的文档驱动方法。使用DOM对XML文件进行操作时，
     * 首先解析器读入整个XML文档到内存中，然后解析全部文件，并将文件分为独立的元素、
     * 属性等，以树结构的形式在内存中对XML文件进行表示，开发人员通过使用DOM API遍
     * 历XML树，根据需要修改文档或检索所需数据
     * 优点：
     * 1、形成了树结构，有助于更好的理解、掌握，且代码容易编写。
     * 2、解析过程中，树结构保存在内存中，方便修改。
     * 缺点：
     * 1、由于文件是一次性读取，所以对内存的耗费比较大。
     * 2、如果XML文件比较大，容易影响解析性能且可能会造成内存溢出
     * <p>
     * 使用情境:
     * 对于像手机这样的移动设备来讲，内存是非常有限的，在XML文档比较小、
     * 需要对解析文档进行一定的操作且一旦解析了文档需要多次访问这些数据的情况下
     * 可以考虑使用DOM方式，因为其检索和解析效率较高
     */
    private void methodDOM() {
        try {
            InputStream stream = getAssets().open("books.xml");

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(stream);

            //得到根节点
            Element element = document.getDocumentElement();
            //获取根节点的所有language的节点
            NodeList bookList = element.getElementsByTagName("book");
            mTextViewXml.append("一共有" + bookList.getLength() + "本书:\n");
            for (int i = 0; i < bookList.getLength(); i++) {
                Node book = bookList.item(i);
                //解析book节点的所有属性
                NamedNodeMap attrs = book.getAttributes();
                for (int j = 0; j < attrs.getLength(); j++) {
                    Node attr = attrs.item(j);
                    mTextViewXml.append(String.format("%s=%s:\n", attr.getNodeName(), attr.getNodeValue()));
                }
                //解析book节点的子节点
                NodeList childNodes = book.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        String key = childNodes.item(j).getNodeName();
                        String value = childNodes.item(j).getFirstChild().getNodeValue();
                        mTextViewXml.append(String.format("\t%s==%s\n", key, value));
                    }
                }
                mTextViewXml.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
}
