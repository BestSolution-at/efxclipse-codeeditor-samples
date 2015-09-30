/// This is a rect
class Rectangle {
  num left;   
  num top; 
  num width;  
  /// This is a height value  
  num height;
         
  
  num get right             => left + width;
      set right(num value)  => left = value - width;
  num get bottom            => top + height;
      set bottom(num value) => top = value - height;
}

/// This is where the app starts executing.
main() {  var r = new Rectangle();
  r . bottom = 10;
  r.height = 10;  r.bottom = 10;
}