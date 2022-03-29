<!DOCTYPE html>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="https://unpkg.com/tachyons@4.12.0/css/tachyons.min.css"/>
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
</head>
<body class="ph3 pt0 pb4 mw7 center sans-serif">
<p class="f5 mt1 mb4 lh-copy">A simple request taking app</p>
<form action="/request" method="POST" enctype="multipart/form-data">
    <ol class="list pl0">
        <li class="mv3"><label class="f6 b db mb2" for="description">Write your content here</label>
            <textarea class="f4 db border-box hover-black w-100 measure ba b--black-20 pa2 br2 mb2" rows="5"
                      name="description"><#if description??>${description}</#if></textarea>
            <input class="f6 link dim br1 ba bw1 ph3 pv2 mb2 dib black bg-white pointer" type="submit" value="Publish"
                   name="publish">
        </li>
    </ol>
</form>
<ul class="list pl0"><p class="f6 b db mb2">Requests</p>
    <#if requests??>
        <#list requests as request>
            <li class="mv3 bb bw2 b--light-yellow bg-washed-yellow ph4 pv2"><p class="measure"></p>
                <p>${request}</p>
                <p></p>
            </li>
        <#else>
            <p class="lh-copy f6">There is no requests yet.</p>
        </#list>
    </#if>
</ul>
</body>
</html>