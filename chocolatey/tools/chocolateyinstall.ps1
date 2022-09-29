$version = '3.6.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'F7C4026297C4CF04F00C61699647EDCE44243E008849CFCC99EA6EAF3C15AE5E'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
