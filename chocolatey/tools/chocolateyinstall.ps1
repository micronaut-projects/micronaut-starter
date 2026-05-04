$version = '5.0.0-M3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '32040762994F44B26659A93294AFCE9A645974BA835ED9CD6D1361CAD35BB484'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
