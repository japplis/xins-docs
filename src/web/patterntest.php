<?
	 $message = "";

	 if (get_magic_quotes_gpc() == 1) {
               $pattern = str_replace("\\\\", "\\", @$pattern);
               $string = str_replace("\\\\", "\\", @$string);
	 }

	if (isset($submit)) {
		$pattern = eregi_replace("^/(\^)?(.*)(\$)?/$", "\\2", $pattern);
		$result = @preg_match("/^".$pattern."$/", $string);

		if ($result === FALSE) {
			$message = "The pattern '$pattern' contains an error";
		} else {
			if ($result === 1) {
			$message = "The string \"$string\" <span style='color:blue'>matches</span> the pattern \"$pattern\"";
			} else {
				$message = "The string \"$string\" does <span style='color:red'>not match</span> the pattern \"$pattern\"";
			}
		}
	}
?>
<html>
	<head>
		<title>Pattern test form</title>
		<link href="pattern-style.css" type="text/css" rel="stylesheet" />
	</head>
	<body>

		<h1>Pattern test form</h1>
		<p>This form can be used to test regular expression patterns.</p>
		<h2>Test form</h2>
		<form method="post" action="patterntest.php">
			<table>
				<tr>
					<td>Pattern:</td>
					<td class="required"><input type="text" name="pattern" value="<?=@$pattern;?>" class="required" /></td>
				</tr>
				<tr>
					<td>String:</td>
					<td class="required"><input type="text" name="string" value="<?=@$string;?>" class="required" /></td>
				</tr>
				<tr>
					<td colspan="2" class="right-align">
						<input type="submit" name="submit" value="submit" />
						<input type="button" onClick="location = 'patterntest.php'" value="Reset" />
					</td>
				</tr>
			</table>
		</form>
		<?=$message;?>
	</body>
</html>
