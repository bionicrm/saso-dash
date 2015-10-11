package io.saso.dash.templating;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;

import java.io.IOException;

public interface Templater
{
    String render(String template, JtwigModelMap modelMap) throws IOException,
            ParseException, CompileException, RenderException;
}
