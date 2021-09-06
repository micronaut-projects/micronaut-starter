$version = '3.0.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'C19984595FD5C8C84D579A6F380BCE52EF9586C23962E63201D9C2FF699A5468'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
