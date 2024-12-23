$version = '4.7.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '0DFB947BEB542367E03E4A8C6755572622C7D4FF58ED18D1CAEF6ABD066C7465'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
