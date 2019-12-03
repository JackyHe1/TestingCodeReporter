TEMPLATE = """
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
  <script>
      window.onload = function(){{
         var obj_lis = document.querySelectorAll('.pathUL li');
         for(i=0;i<obj_lis.length;i++){{
             obj_lis[i].onclick = function(){{
                 var path = this.innerHTML;
                 clickPath(path);
             }}
         }}
      }}
      function clickPath(path){{
         var lis = document.querySelectorAll('.codeUL li');
         for(var i = 0; i < lis.length; i++) {{
             lis[i].className = 'list-group-item borderless border-0';
         }}

         var linesArray = path.split(',').map(Number)
         for(var i = 0; i < linesArray.length; i++) {{
             lis[linesArray[i] - 1].className = 'list-group-item list-group-item-success border-0';
         }}
      }}

    function nodeFunction() {{
      document.getElementById("nodes").style.display = "block";
      document.getElementById("edges").style.display = "none";
      document.getElementById("edgePairs").style.display = "none";
      document.getElementById("simplePaths").style.display = "none";
      document.getElementById("primePaths").style.display = "none";
    }}
    function edgeFunction() {{
      document.getElementById("nodes").style.display = "none";
      document.getElementById("edges").style.display = "block";
      document.getElementById("edgePairs").style.display = "none";
      document.getElementById("simplePaths").style.display = "none";
      document.getElementById("primePaths").style.display = "none";
    }}
    function edgePairFunction() {{
      document.getElementById("nodes").style.display = "none";
      document.getElementById("edges").style.display = "none";
      document.getElementById("edgePairs").style.display = "block";
      document.getElementById("simplePaths").style.display = "none";
      document.getElementById("primePaths").style.display = "none";
    }}
    function simplePathFunction() {{
      document.getElementById("nodes").style.display = "none";
      document.getElementById("edges").style.display = "none";
      document.getElementById("edgePairs").style.display = "none";
      document.getElementById("simplePaths").style.display = "block";
      document.getElementById("primePaths").style.display = "none";
    }}
    function primePathFunction() {{
      document.getElementById("nodes").style.display = "none";
      document.getElementById("edges").style.display = "none";
      document.getElementById("edgePairs").style.display = "none";
      document.getElementById("simplePaths").style.display = "none";
      document.getElementById("primePaths").style.display = "block";
    }}
  </script>

</head>
<style>
    li {{
	    font-family: Lucida Console,Lucida Sans Typewriter,monaco,Bitstream Vera Sans Mono,monospace;
      padding-top: 0.25rem;
      padding-bottom: 0.25rem;
    }}
    .overlay {{
      position: absolute;
      width: 100%;

    }}
</style>
<body>

<div class="container">
  <h2>List Group With Contextual Classes</h2>
  <div class="container">
  <div class="row">
  <div class="col-sm border">
  <ul class="list-group codeUL">
  {}
  </ul>
  </div>

  <div class="col-sm">
<ul class="nav nav-tabs">
  <li class="nav-item">
    <a class="nav-link" onclick="nodeFunction()" href="#">Node</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="edgeFunction()" href="#">Edge</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="edgePairFunction()" href="#">EdgePair</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="simplePathFunction()" href="#">SimplePath</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="primePathFunction()" href="#">PrimePath</a>
  </li>
</ul>

  <ul id="nodes" class="list-group overlay pathUL" style="">
    {}
  </ul>
  <ul id="edges" class="list-group overlay pathUL" style="display=none;">
    {}
  </ul>
  <ul id="edgePairs" class="list-group overlay pathUL" style="display=none;">
    {}
  </ul>
  <ul id="simplePaths" class="list-group overlay pathUL" style="display=none;">
    {}
  </ul>
  <ul id="primePaths" class="list-group overlay pathUL" style="display=none;">
    {}
  </ul>

  </div>

  </div>
  </div>
</div>

</body>
</html>
"""

def generate(filename):
    with open(filename + '.txt') as f:
        lines = f.readlines() 
    code = []
    for i, line in enumerate(lines):
        # l = '<li class="list-group-item" style="padding-left:{}em">'
        l = '<li class="list-group-item list-group-item-warning">'
        l = l.format(len(line) - len(line.strip()), i + 1)
        l += line[:-1]
        l += '</li>'
        code.append(l)
    return '\n'.join(code)


def generate_html(lines):
    code = []
    for i, line in enumerate(lines):
        # l = '<li class="list-group-item" style="padding-left:{}em">'
        l = '<li class="list-group-item border-0" style="padding-top: 0.25rem; padding-bottom: 0.25rem"> <code style="padding-right:{}em"> {} </code>'
        l = l.format(len(line) - len(line.strip()), i + 1)
        l += line[:-1]
        l += '</li>'
        code.append(l)

    return TEMPLATE.format('\n'.join(code), generate('nodes'), generate('edges'), generate('edgePairs'), generate('simplePaths'), generate('primePaths'))


with open('Demo.java') as f:
    lines = f.readlines()

html = generate_html(lines)

with open('code.html', 'w') as f:
    f.write(html)
