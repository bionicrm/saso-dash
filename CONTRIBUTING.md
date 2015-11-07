# Contributing

**Note:** While it's nice for contributors to follow these guidelines, mistakes here and there won't get PR's rejected; they'll be corrected later.

### Style

- 80 characters per line except:
  - import statements
  - in Javadocs where `{@code ...}` tags cannot reasonably be split into multiple lines
- Brackets on new line after:
  - class headers
  - method headers
- Brackets on same line for all statements not in the list above; brackets are always required for these
- No tabs; 4 spaces for indentation
- All files end with blank line
- Maximum of 1 blank line between two consecutive lines of code

Well-formatted example:
  
```java
public class MyClass implements MyInterface
{
    private static final int MAGIC_NUM = 7;
    
    private final int randomNum;
    
    @Inject
    public MyClass(@Named("randomNum") int randomNum)
    {
        this.randomNum = randomNum;
    }

    @Override
    public void doSomething()
    {
        if (true) {
            /* ... */
        }
        else {
            /* ... */
        }
        
        for (int i = 0; i < MAGIC_NUM; i++) {
            System.out.println(i);
        }
    }
    
    /**
     * This documents a method. Essentially all interface methods should be
     * documented like this, along with private helper methods.
     *
     * @param str a string that holds information
     *
     * @return a double that represents how many kittehs exist
     */
    private double helper(String str)
    {
        /* ... */
        
        // don't cast if unnecessary, and don't do `9_001d` or
        // `9_001.0`
        return 9_001;
    }
}
```
