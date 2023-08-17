$version = '3.10.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '3DCD8795C59014656635F36CEDE85DC7BC1085A7D231CA8933DA21041E19AE1C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
