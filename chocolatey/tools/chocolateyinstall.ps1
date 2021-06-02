$version = '2.5.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '7A27062B299B41CED9AFA92BA75A8B756F03698D0B27B9E3192FC6F6B3F466F8'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
