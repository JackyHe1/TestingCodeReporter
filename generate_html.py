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
             obj_lis[i].onmouseover = function(){{
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
      document.getElementById("nodesCoverage").style.display = "block";
      document.getElementById("edgesCoverage").style.display = "none";
      document.getElementById("edgePairsCoverage").style.display = "none";
      document.getElementById("simplePathsCoverage").style.display = "none";
      document.getElementById("primePathsCoverage").style.display = "none";
    }}
    function edgeFunction() {{
      document.getElementById("nodes").style.display = "none";
      document.getElementById("edges").style.display = "block";
      document.getElementById("edgePairs").style.display = "none";
      document.getElementById("simplePaths").style.display = "none";
      document.getElementById("primePaths").style.display = "none";
      document.getElementById("nodesCoverage").style.display = "none";
      document.getElementById("edgesCoverage").style.display = "block";
      document.getElementById("edgePairsCoverage").style.display = "none";
      document.getElementById("simplePathsCoverage").style.display = "none";
      document.getElementById("primePathsCoverage").style.display = "none";
    }}
    function edgePairFunction() {{
      document.getElementById("nodes").style.display = "none";
      document.getElementById("edges").style.display = "none";
      document.getElementById("edgePairs").style.display = "block";
      document.getElementById("simplePaths").style.display = "none";
      document.getElementById("primePaths").style.display = "none";
      document.getElementById("nodesCoverage").style.display = "none";
      document.getElementById("edgesCoverage").style.display = "none";
      document.getElementById("edgePairsCoverage").style.display = "block";
      document.getElementById("simplePathsCoverage").style.display = "none";
      document.getElementById("primePathsCoverage").style.display = "none";
    }}
    function simplePathFunction() {{
      document.getElementById("nodes").style.display = "none";
      document.getElementById("edges").style.display = "none";
      document.getElementById("edgePairs").style.display = "none";
      document.getElementById("simplePaths").style.display = "block";
      document.getElementById("primePaths").style.display = "none";
      document.getElementById("nodesCoverage").style.display = "none";
      document.getElementById("edgesCoverage").style.display = "none";
      document.getElementById("edgePairsCoverage").style.display = "none";
      document.getElementById("simplePathsCoverage").style.display = "block";
      document.getElementById("primePathsCoverage").style.display = "none";
    }}
    function primePathFunction() {{
      document.getElementById("nodes").style.display = "none";
      document.getElementById("edges").style.display = "none";
      document.getElementById("edgePairs").style.display = "none";
      document.getElementById("simplePaths").style.display = "none";
      document.getElementById("primePaths").style.display = "block";
      document.getElementById("nodesCoverage").style.display = "none";
      document.getElementById("edgesCoverage").style.display = "none";
      document.getElementById("edgePairsCoverage").style.display = "none";
      document.getElementById("simplePathsCoverage").style.display = "none";
      document.getElementById("primePathsCoverage").style.display = "block";
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
    .nav-link {{
      padding: 0.5rem
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
  <div class="container">
<ul class="nav nav-tabs"  >
  <li class="nav-item">
    <a class="nav-link" onclick="nodeFunction()" >Node</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="edgeFunction()" >Edge</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="edgePairFunction()" >EdgePair</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="simplePathFunction()" >SimplePath</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="primePathFunction()" >PrimePath</a>
  </li>
</ul>
</div>

  <div class="container">
  <div class="row">

  <div class="col-sm">
  <ul id="nodesCoverage" class="list-group overlay pathUL" style="">
    {}
  </ul>
  <ul id="edgesCoverage" class="list-group overlay pathUL" style="display=none;">
    {}
  </ul>
  <ul id="edgePairsCoverage" class="list-group overlay pathUL" style="display=none;">
    {}
  </ul>
  <ul id="simplePathsCoverage" class="list-group overlay pathUL" style="display=none;">
    {}
  </ul>
  <ul id="primePathsCoverage" class="list-group overlay pathUL" style="display=none;">
    {}
  </ul>
  </div>

  <div class="col-sm">
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
  </div>
</div>

</body>
</html>
"""

def generate(filename, tag="list-group-item-warning"):
    with open('out/' + filename + '.txt') as f:
        lines = f.readlines() 
    code = []
    for i, line in enumerate(lines):
        # l = '<li class="list-group-item" style="padding-left:{}em">'
        l = '<li class="list-group-item {}">'.format(tag)
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

    return TEMPLATE.format('\n'.join(code), generate('nodes_coverage'), generate('edges_coverage'), generate('edgePairs_coverage'), generate('simplePaths_coverage'), generate('primePaths_coverage'),
                generate('nodes', 'list-group-item-secondary'), generate('edges', 'list-group-item-secondary'), generate('edgePairs', 'list-group-item-secondary'), generate('simplePaths', 'list-group-item-secondary'), generate('primePaths', 'list-group-item-secondary'))


with open('src/Demo.java') as f:
    lines = f.readlines()

html = generate_html(lines)

with open('out/code.html', 'w') as f:
    f.write(html)
