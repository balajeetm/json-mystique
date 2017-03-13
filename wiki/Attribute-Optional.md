The features of optional are as below

* Optional is a boolean defaulted to false
* Not all transformations from a turn will return a non null value
* Denotes if a transformed null value, should be copied to the output
* If optional is true and the transformed value is null, the value is not copied to the output

In the packaging for the road trip example, look at this as compartmentalization.
Let's presume that a backpack has predefined compartments for pant, shirt, shoes, etc.
So even if you do not have a pant, there will be a compartment for pant with nothing in it.
This is like copying a null value to output. There is a compartment, but no item in it.

However, there are no pre-allocated compartment for a suit. If you want to pack a suit, you make space for it.
So if the suit doesn't exist, you do not make space for it. Optional decides, if you need to make space of not
