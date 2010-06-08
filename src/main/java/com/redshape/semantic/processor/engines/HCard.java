package com.redshape.semantic.processor.engines;

import com.redshape.semantic.data.hcard.HCardElement;
import com.redshape.semantic.processor.IProcessor;
import com.redshape.semantic.processor.ProcessingException;
import com.vio.utils.BeansLoader;
import com.vio.utils.ObjectsLoaderException;
import com.vio.utils.Registry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 7, 2010
 * Time: 4:03:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class HCard implements IProcessor {
    /**
     * HCard root part
     */
    private HCardElement rootPart;

    private XPath xpath;

    /**
     * Beans loader
     *
     * @var BeansLoader
     */
    private BeansLoader beanLoader;

    public HCard() {
        this.xpath = XPathFactory.newInstance().newXPath();
    }

    public void processDefinition( File file ) throws ProcessingException {
        try {
            this.getBeanLoader().loadObject( this.rootPart, file );
        } catch ( ObjectsLoaderException e ) {
            throw new ProcessingException();
        }
    }

    public BeansLoader getBeanLoader() {
        return this.beanLoader;
    }

    public void setBeanLoader( BeansLoader loader ) {
        this.beanLoader = loader;
    }

    protected Document buildDocument( File file ) throws ProcessingException {
        try {
            return DocumentBuilderFactory.newInstance()
                                     .newDocumentBuilder()
                                        .parse(file);
        } catch ( Throwable e ) {
            throw new ProcessingException();
        }
    }

    protected Document buildDocument( String data ) throws ProcessingException {
        try {
            return DocumentBuilderFactory.newInstance()
                                         .newDocumentBuilder()
                                         .parse(data);
        } catch ( Throwable e ) {
            throw new ProcessingException();
        }
    }

    public HCardElement process( File file ) throws IOException, ProcessingException {
        return this.process( Registry.getResourcesLoader().loadData(file) );
    }

    public HCardElement process( String data ) throws ProcessingException {
        return this.process( this.buildDocument( data ) );
    }

    public HCardElement process( Document document ) throws ProcessingException {
        return this.process( this.rootPart, document.getDocumentElement() );
    }

    protected HCardElement process( HCardElement element, Node node ) throws ProcessingException {
        if ( element.hasChilds() ) {
            for ( HCardElement elementChild : element.getChilds() ) {
                this.process( elementChild, this.findNode( elementChild, node ) );
            }
        } else {
            element.setValue( this.getNodeValue(element, this.findNode( element, node ) ) );
        }

        if (  element.getValue() == null && element.hasChilds() ) {
            HCardElement typeElement = element.getByName("type");
            element.setValue(
                this._getNodeValue(
                    typeElement,
                    this.findNode( typeElement, node )
                )
            );
        }

        return this.optimizeTree( element );
    }

    protected String getNodeValue( HCardElement element, Node context ) {
        String value = null;
        for ( String valueHolderName : element.getValueHolders() ) {
            Node valueHolderNode = context.getAttributes().getNamedItem(valueHolderName);
            if ( valueHolderNode == null ) {
                continue;
            }

            if ( valueHolderNode.getNodeValue().isEmpty() ) {
                continue;
            }

            value = valueHolderNode.getNodeValue();
        }

        return value;
    }

    protected String _getNodeValue( HCardElement element, Node context ) {
        StringBuilder value = new StringBuilder();

        boolean found = false;
        for ( Node contextSibling = context.getNextSibling(); !found && contextSibling != null; contextSibling = contextSibling.getNextSibling() ) {
            switch ( contextSibling.getNodeType() ) {
                case Node.ELEMENT_NODE:
                    if ( !this.isFunctionalNode(contextSibling) ) {
                        value.append( contextSibling.getNodeValue() );
                    } else {
                        found = true;
                    }
                break;
                case Node.TEXT_NODE:
                    if ( !contextSibling.getNodeValue().isEmpty() ) {
                        value.append( contextSibling.getNodeValue() );
                    }
                break;
            }
        }

        return null;
    }

    protected boolean isFunctionalNode( Node node ) {
        return this.isFunctionalNode( node, this.rootPart.getChilds() );
    }

    protected boolean isFunctionalNode( Node node, HCardElement element ) {
        Node classAttribute = node.getAttributes().getNamedItem("class");
        Node idAttribute = node.getAttributes().getNamedItem("id");
        Node relAttribute = node.getAttributes().getNamedItem("rel");

        if ( classAttribute != null && classAttribute.getNodeValue().contains( element.getName() ) ) {
            return true;
        }

        if ( idAttribute != null && idAttribute.getNodeValue().contains( element.getName() ) ) {
            return true;
        }

        if ( relAttribute != null && relAttribute.getNodeValue().contains( element.getName() ) ) {
            return true;
        }

        return this.isFunctionalNode( node, element.getChilds() );
    }

    protected boolean isFunctionalNode( Node node, Collection<HCardElement> element ) {
        for ( HCardElement childElement : element ) {
            if ( this.isFunctionalNode( node, childElement ) ) {
                return true;
            }
        }

        return false;
    }

    protected Node findNode( HCardElement element, Node context ) throws ProcessingException {
        try {
            XPathExpression xpathEval = this.xpath.compile("*[@id='" + element.getName() + "' " +
                                                           " or contains( @class, normalize-spaces(" + element.getName() + ")" +
                                                                " or @rel=normalize-spaces(" + element.getName() + ") ");

            return (Node) xpathEval.evaluate( context, XPathConstants.NODE );
        } catch ( Throwable e ) {
            throw new ProcessingException();
        }
    }

    protected HCardElement optimizeTree( HCardElement rootElement ) throws ProcessingException {
        

        return rootElement;
    }

}
