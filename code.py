TEMPLATE = """
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<style>
    li {{ 
	    font-family: Lucida Console,Lucida Sans Typewriter,monaco,Bitstream Vera Sans Mono,monospace; 
    }}
    li.borderless {{ border-top: 0 none; }}
</style>
<body>

<div class="container">
  <h2>List Group With Contextual Classes</h2>
  <ul class="list-group">
  {}
    <li class="list-group-item list-group-item-success">First item</li>
    <li class="list-group-item list-group-item-info">Second item</li>
    <li class="list-group-item list-group-item-warning">Third item</li>
    <li class="list-group-item list-group-item-danger">Fourth item</li>
  </ul>
</div>

</body>
</html>
"""



def generate_html(lines):
    code = []
    for line in lines:
        # l = '<li class="list-group-item" style="padding-left:{}em">'
        l = '<li class="list-group-item borderless" style="padding-left:{}em">'
        l = l.format(len(line) - len(line.strip()) - 1)
        l += line[:-1]
        l += '</li>'
        code.append(l)

    return TEMPLATE.format('\n'.join(code))


with open('Demo.java') as f:
    lines = f.readlines()

html = generate_html(lines)

with open('code.html', 'w') as f:
    f.write(html)