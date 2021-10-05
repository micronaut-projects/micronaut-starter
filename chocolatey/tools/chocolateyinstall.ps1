$version = '3.0.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'BE814784DABF0237B9ABA146389A9F3BEB97FABC5AD73F430B359C2BED3F064E'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
