$version = '4.10.15'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '278D4E273646581EEBEF459A0C3087DE7871D5EA967AB70C7CCFE3DBF2EE103C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
