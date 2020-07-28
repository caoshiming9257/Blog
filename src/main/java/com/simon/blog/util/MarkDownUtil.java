package com.simon.blog.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;

import java.util.*;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/20 15:41
 * @Description: 将markdown文本转换为HTML格式
 */
public class MarkDownUtil {

    /**
      @Description: 基本用法
      @return: java.lang.String
      @Author: Simon_Cao
      @Date: 2020/7/20
     */
    public static String markdownToHTML(){
        Parser parser = Parser.builder().build();
        Node document = parser.parse("This is *Sparta*");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 增加扩展(标题锚点，表格生成)
     * MarkDown转换为HTML
     * **/
    public static String markdownToHtmlExtensions(String markdown){
        /*h标签生成id*/
        Set<Extension> singleton = Collections.singleton(HeadingAnchorExtension.create());
        /*将table转换为HTML*/
        List<Extension> extensions = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(extensions)
                .build();
        Node node = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(singleton)
                .extensions(extensions)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(AttributeProviderContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(node);
    }

    /**
     * 处理标签的属性
     * **/
    static class CustomAttributeProvider implements AttributeProvider{

        @Override
        public void setAttributes(Node node, String s, Map<String, String> map) {
            /*改变a标签的target属性为_blank*/
            if (node instanceof Link){
                map.put("target","_blank");
            }
            if (node instanceof TableBlock){
                map.put("class","ui celled table");
            }
        }
    }
}
