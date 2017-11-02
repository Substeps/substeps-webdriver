/*
 *  Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.webdriver.substeps.runner;

import com.technophobia.substeps.execution.node.IExecutionNode;
import com.technophobia.substeps.runner.IExecutionListener;
import com.technophobia.substeps.runner.MutableSupplier;
import com.technophobia.webdriver.util.WebDriverContext;

/**
 * Listener class used to set failure state on the WebdriverContext
 */
public class TestFailureListener implements IExecutionListener {

    private final MutableSupplier<WebDriverContext> webDriverContextSupplier;

    /**
     * default constructor
     * @param webDriverContextSupplier the supplier class of the WebdriverContext
     */
    public TestFailureListener(final MutableSupplier<WebDriverContext> webDriverContextSupplier) {
        super();
        this.webDriverContextSupplier = webDriverContextSupplier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNodeFailed(final IExecutionNode arg0, final Throwable arg1) {

        final WebDriverContext webDriverContext = this.webDriverContextSupplier.get();
        // possible to have a failure before the webdrivercontext has been
        // initialised - missing ' default.webdriver.timeout.secs' property for
        // example
        if (webDriverContext != null) {
            webDriverContext.setFailed();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNodeFinished(final IExecutionNode arg0) {
        // no op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNodeStarted(final IExecutionNode arg0) {
        // no op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNodeIgnored(final IExecutionNode node) {
        // no op
    }
}
