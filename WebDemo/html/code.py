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
    function edgeFunction() {{
      var edges = document.getElementById("edges");
      edges.style.display = "block";
      var paths = document.getElementById("paths");
      paths.style.display = "none";
    }}
    function pathFunction() {{
      var edges = document.getElementById("edges");
      edges.style.display = "none";
      var paths = document.getElementById("paths");
      paths.style.display = "block";
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
  <ul class="list-group">
  {}
  </ul>
  </div>

  <div class="col-sm">
<ul class="nav nav-tabs">
  <li class="nav-item">
    <a class="nav-link" onclick="pathFunction()" href="#">Node</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="edgeFunction()" href="#">Edge</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="pathFunction()" href="#">EdgePair</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="pathFunction()" href="#">SimplePath</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" onclick="pathFunction()" href="#">PrimePath</a>
  </li>
</ul>

  <ul id="edges" class="list-group overlay" style="">
    {}
    <li class="list-group-item list-group-item-success">First item</li>
    <li class="list-group-item list-group-item-info">Second item</li>
    <li class="list-group-item list-group-item-warning">Third item</li>
    <li class="list-group-item list-group-item-danger">Fourth item</li>
  </ul>
  <ul id="paths" class="list-group overlay" style="display=block;">

    <li class="list-group-item list-group-item-success">First item</li>
    <li class="list-group-item list-group-item-info">Second item</li>
    <li class="list-group-item list-group-item-warning">Third item</li>
    <li class="list-group-item list-group-item-danger">Fourth item</li>
  </ul>

  </div>

  </div>
  </div>
</div>

</body>
</html>
"""



def generate_html(lines, edges):
    code = []
    for i, line in enumerate(lines):
        # l = '<li class="list-group-item" style="padding-left:{}em">'
        l = '<li class="list-group-item border-0" style="padding-top: 0.25rem; padding-bottom: 0.25rem"> <code style="padding-right:{}em"> {} </code>'
        l = l.format(len(line) - len(line.strip()), i + 1)
        l += line[:-1]
        l += '</li>'
        code.append(l)

    edge_code = []
    for edge in edges:
        l = '<li class="list-group-item list-group-item-warning">'
        l += edge.strip()
        l += '</li>'
        edge_code.append(l)

    return TEMPLATE.format('\n'.join(code), '\n'.join(edge_code))


with open('Demo.java') as f:
    lines = f.readlines()

with open('edges.txt') as f:
    edges = f.readlines()

html = generate_html(lines, edges)

with open('code.html', 'w') as f:
    f.write(html)