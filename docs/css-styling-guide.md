# CSS Styling Guide for Pixelle

## Text Styling

### Font Weight

To set text weight to bold in JavaFX CSS, use the `-fx-font-weight: bold;` property. 

There are two ways to apply bold text in Pixelle:

1. **Using the `.text-bold` class**:
   ```xml
   <Label text="This text will be bold" styleClass="text-bold"/>
   ```
   
   You can also combine it with other style classes:
   ```xml
   <Label text="Bold and large text" styleClass="text-lg, text-bold"/>
   ```

2. **Inline styling**:
   ```xml
   <Label text="This text will be bold" style="-fx-font-weight: bold;"/>
   ```

### Examples

#### Making a Label Bold
```xml
<Label text="Bold Text Example" styleClass="text-bold"/>
```

#### Making a Button Text Bold
```xml
<Button text="Bold Button" styleClass="text-bold"/>
```

#### Combining with Other Styles
```xml
<Label text="Large Bold Text" styleClass="text-lg, text-bold"/>
```

## Other Text Styling Options

- Font size: `-fx-font-size: 16px;`
- Text color: `-fx-text-fill: white;`
- Font family: `-fx-font-family: "Arial";`

## Existing Style Classes

Pixelle already includes several text-related style classes:

- `.text-sm`: Small text (12px)
- `.text-md`: Medium text (16px)
- `.text-lg`: Large text (25px)
- `.text-bold`: Bold text

You can combine these classes as needed for your UI elements.