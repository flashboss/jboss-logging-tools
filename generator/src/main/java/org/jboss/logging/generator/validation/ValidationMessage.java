/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.logging.generator.validation;

import javax.lang.model.element.Element;

/**
 * Date: 09.04.2011
 *
 * @author <a href="mailto:jrperkinsjr@gmail.com">James R. Perkins</a>
 */
public interface ValidationMessage {

    /**
     * Validation message type enum.
     */
    public enum MessageType {
        ERROR,
        WARN
    }

    /**
     * The type of the message.
     *
     * @return the type of the message.
     */
    MessageType type();

    /**
     * Returns the element that caused the error.
     *
     * @return the element that caused the error.
     */
    Element getElement();

    /**
     * Returns the error message.
     *
     * @return the error message.
     */
    String getMessage();
}
