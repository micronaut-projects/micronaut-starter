$version = '4.8.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '967FF5D8CAC0E712737464481CCB1D21DED001B52541560161BC1378AA667BB0'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
