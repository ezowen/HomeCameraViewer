<?php
    //set GET variables to local variables
    $month  = $_GET['month'];
    $day    = $_GET['day'];
    $hour   = $_GET['hour'];
    $minute = $_GET['minute'];
    $what   = $_GET['what'];
    
    //set up query and get all images from img directory
    $query  = $month.'_'.$day.'_'.$hour.'_'.$minute;
    $imgdir = "cam1";
    $images = scandir($imgdir);
    $images = array_diff($images, array('..', '.'));

    //get all images that match query and count number of matches
    $html_imgs = "";
    $imgCount = 0;
    foreach ($images as $img) {
        if (strpos($img, $query) !== false) {
            // 71.197.6.239
            $html_imgs .= "<img src=\"http://71.197.6.239/netapps/project1/$imgdir/$img\">\n";
            $imgCount = $imgCount + 1;
        }
    }
    
    if ($html_imgs == "") {
        $html_imgs = "There are no images that match your search. The following images are available:<br><br>
                      Format: Month_Day_Hour_Minute<br>";
        foreach($images as $img) 
            $html_imgs .= str_replace(".bmp", "", $img) . "<br>";
    }
   
    //decide what to return based on 'what' variable
    $html = "";
    if ($what == "imgs")
        $html = $html_imgs;
    else if ($what == "nmbr")
        $html = "Currently viewing ".$imgCount." images.";

    //return html
    echo "<html>
            <head>
            </head>
            <body>
              $html
            </body>
          </html>";
?>
