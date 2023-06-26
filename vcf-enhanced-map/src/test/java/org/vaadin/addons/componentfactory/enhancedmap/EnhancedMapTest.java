package org.vaadin.addons.componentfactory.enhancedmap;

import org.junit.After;
import org.junit.Before;

import com.vaadin.flow.component.UI;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class EnhancedMapTest {

    private UI ui;

    @Before
    public void setUp() {
        ui = new UI();
        UI.setCurrent(ui);
    }

    @After
    public void tearDown() {
        UI.setCurrent(null);
    }

}
