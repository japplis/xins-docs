function togglemenu(flag) {

   // IDs of the elements
   var idMenu    = 'menu';
   var idContent = 'content';
   var idShow    = 'showmenu';
   var idHide    = 'hidemenu';

   var menu    = null;
   var content = null;
   var show    = null;
   var hide    = null;

   // Standards-based approach: getElementById
   if (document.getElementById) {
      menu    = document.getElementById(idMenu).style;
      content = document.getElementById(idContent).style;
      show    = document.getElementById(idShow).style;
      hide    = document.getElementById(idHide).style;

   // Some MSIE versions
   } else if (document.all) {
      menu    = document.all[idMenu].style;
      content = document.all[idContent].style;
      show    = document.all[idShow].style;
      hide    = document.all[idHide].style;

   // Otherwise: error
   } else {
      alert("Error: Your browser is not supported. Supported browsers include Internet Explorer 5.0+, Mozilla 1.0+, Firefox 1.0+, Opera 8.0+ and Konqueror 3.4+.");
      return;
   }

   // Toggle the display
   if (flag == true) {
      menu.display       = "block";
      content.marginLeft = "13em";
      show.display       = "none";
      hide.display       = "inline";
   } else {
      menu.display       = "none";
      content.marginLeft = "1em";
      show.display       = "inline";
      hide.display       = "none";
   }
}
