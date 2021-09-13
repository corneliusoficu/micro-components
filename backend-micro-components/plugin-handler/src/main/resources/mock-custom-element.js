class EngineAssemble extends HTMLElement {
    constructor() {
      // Always call super first in constructor
      super();

      // Create a shadow root
      const shadow = this.attachShadow({mode: 'open'});

      // Create spans
      const wrapper = document.createElement('div');
      var x = document.createElement("H1");
      var t = document.createTextNode("Engine was assembled");
      x.appendChild(t);
      wrapper.appendChild(x)

      shadow.appendChild(wrapper)
    }
  }

  // Define the new element
  customElements.define('engine-assemble', EngineAssemble);