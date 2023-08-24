$version = '4.0.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '57FE0DB8CB3116783FB2E495BED1B805CD1B8CF8CA2C92CD6DBB1DF5A2E1C6E5'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
