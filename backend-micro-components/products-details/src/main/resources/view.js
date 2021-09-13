class ProductDetails extends HTMLElement {
    constructor() {
      // Always call super first in constructor
      super();

      // Create a shadow root
      const shadow = this.attachShadow({mode: 'open'});

      // Create spans
      const wrapper = document.createElement('div');
      var x = document.createElement("H1");
      var t = document.createTextNode("Product details for item!");
      x.appendChild(t);
      wrapper.appendChild(x)

      shadow.appendChild(wrapper)
    }
  }

  // Define the new element
  customElements.define('product-details', ProductDetails);