TEMPLATE = """
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Coverage</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

<style>
.chartdiv {{
  width: 100%;
  height: 300px;
}}

</style>

<!-- Resources -->
<script src="https://www.amcharts.com/lib/4/core.js"></script>
<script src="https://www.amcharts.com/lib/4/charts.js"></script>
<script src="https://www.amcharts.com/lib/4/themes/animated.js"></script>

{}

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
      document.getElementById("nodesChart").style.display = "block";
      document.getElementById("edgesChart").style.display = "none";
      document.getElementById("edgePairsChart").style.display = "none";
      document.getElementById("simplePathsChart").style.display = "none";
      document.getElementById("primePathsChart").style.display = "none";
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
      document.getElementById("nodesChart").style.display = "none";
      document.getElementById("edgesChart").style.display = "block";
      document.getElementById("edgePairsChart").style.display = "none";
      document.getElementById("simplePathsChart").style.display = "none";
      document.getElementById("primePathsChart").style.display = "none";
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
      document.getElementById("nodesChart").style.display = "none";
      document.getElementById("edgesChart").style.display = "none";
      document.getElementById("edgePairsChart").style.display = "block";
      document.getElementById("simplePathsChart").style.display = "none";
      document.getElementById("primePathsChart").style.display = "none";
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
      document.getElementById("nodesChart").style.display = "none";
      document.getElementById("edgesChart").style.display = "none";
      document.getElementById("edgePairsChart").style.display = "none";
      document.getElementById("simplePathsChart").style.display = "block";
      document.getElementById("primePathsChart").style.display = "none";
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
      document.getElementById("nodesChart").style.display = "none";
      document.getElementById("edgesChart").style.display = "none";
      document.getElementById("edgePairsChart").style.display = "none";
      document.getElementById("simplePathsChart").style.display = "none";
      document.getElementById("primePathsChart").style.display = "block";
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
  <br/>
  <h2>Coverage</h2>
  <br/>
  <div class="container">
  <div class="row">
  <div class="col-sm border">
  <ul class="list-group codeUL">
  {}
  </ul>
  <div id="nodesChart" class="overlay chartdiv" style="display=none"></div>
  <div id="edgesChart" class="overlay chartdiv" style="display=none"></div>
  <div id="edgePairsChart" class="overlay chartdiv" style="display=none"></div>
  <div id="simplePathsChart" class="overlay chartdiv" style="display=block"></div>
  <div id="primePathsChart" class="overlay chartdiv" style="display=none"></div>
  </div>

  <div class="col-sm">
  <div class="container">
<ul class="nav nav-tabs " style="width=100%" >
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

CHART_TEMPLATE = """
<!-- Chart code -->
<script>
am4core.ready(function() {{

// Themes begin
am4core.useTheme(am4themes_animated);
// Themes end

// Create chart instance
var chart = am4core.create("{}", am4charts.PieChart);

// Add data
chart.data = [ {{
  "country": "NotCovered",
  "litres": {}
}}, {{
  "country": "Covered",
  "litres": {}
}} ];

// Add and configure Series
var pieSeries = chart.series.push(new am4charts.PieSeries());
pieSeries.dataFields.value = "litres";
pieSeries.dataFields.category = "country";
pieSeries.slices.template.stroke = am4core.color("#fff");
pieSeries.slices.template.strokeWidth = 2;
pieSeries.slices.template.strokeOpacity = 1;

// This creates initial animation
pieSeries.hiddenState.properties.opacity = 1;
pieSeries.hiddenState.properties.endAngle = -90;
pieSeries.hiddenState.properties.startAngle = -90;

}}); // end am4core.ready()
</script>>
"""


def generate(filename, tag="list-group-item-warning"):
    with open('out/' + filename + '.txt') as f:
        lines = f.readlines() 
    text = 'Covered'
    if tag != "list-group-item-warning":
      text = 'Total'
    code = ['<li class="list-group-item "> {} </li>'.format(text)]
    for i, line in enumerate(lines):
        # l = '<li class="list-group-item" style="padding-left:{}em">'
        l = '<li class="list-group-item {}">'.format(tag)
        l = l.format(len(line) - len(line.strip()), i + 1)
        l += line[:-1]
        l += '</li>'
        code.append(l)
    return '\n'.join(code)

def generate_chart(coverage):
    res = ''
    for key in coverage:
        res += CHART_TEMPLATE.format(key+'Chart', len(coverage[key]['covered']), len(coverage[key]['total']) - len(coverage[key]['covered']))
    return res

def get_coverage():
    c = dict()
    for key in ['nodes', 'edges', 'edgePairs', 'simplePaths', 'primePaths']:
        with open('out/' + key + '.txt') as f:
            total = f.readlines()
        with open('out/' + key + '_coverage.txt') as f:
            covered = f.readlines()
        c[key] = {
          'covered': covered,
          'total': total
        }
    return c

def generate_html(lines):
    code = []
    for i, line in enumerate(lines):
        # l = '<li class="list-group-item" style="padding-left:{}em">'
        l = '<li class="list-group-item border-0" style="padding-top: 0.25rem; padding-bottom: 0.25rem"> <code style="padding-right:{}em"> {} </code>'
        l = l.format(len(line) - len(line.strip()), i + 1)
        l += line[:-1]
        l += '</li>'
        code.append(l)

    return TEMPLATE.format(generate_chart(get_coverage()), '\n'.join(code), generate('nodes_coverage'), generate('edges_coverage'), generate('edgePairs_coverage'), generate('simplePaths_coverage'), generate('primePaths_coverage'),
                generate('nodes', 'list-group-item-secondary'), generate('edges', 'list-group-item-secondary'), generate('edgePairs', 'list-group-item-secondary'), generate('simplePaths', 'list-group-item-secondary'), generate('primePaths', 'list-group-item-secondary'))


with open('src/Demo.java') as f:
    lines = f.readlines()

html = generate_html(lines)

with open('out/code.html', 'w') as f:
    f.write(html)
